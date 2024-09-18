package com.xiaoke.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoke.common.core.constant.KubeConstants;
import com.xiaoke.common.core.utils.JsonUtils;
import com.xiaoke.entity.kube.entity.*;
import com.xiaoke.entity.kube.vo.AppVO;
import com.xiaoke.extend.kube.PodService;
import com.xiaoke.model.kube.service.AppService;
import com.xiaoke.model.kube.service.*;
import com.xiaoke.tenant.TenantContextHolder;
import com.xiaoke.utils.JoinUtil;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1PodStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@EnableScheduling
@AllArgsConstructor
public class PodTaskService {

    private final AppService appService;
    private final WebSocketService webSocketService;
    private final NamespaceService namespaceService;
    private final ErrorRecordService errorRecordService;
    private final PushUserService pushUserService;
    private final AppPodRecordService appPodRecordService;
    private final PodService podService;


    @Async("taskExecutor")
    public void runJobTask() {
        try {
            //获取命名空间
            QueryWrapper<Namespace> queryNamespace = Wrappers.query();
            List<Namespace> allNameSpace = namespaceService.list(queryNamespace);
            V1PodList v1PodListAll = podService.getPods();
            if (v1PodListAll == null) {
                return;
            }
            for (Namespace nameSpace : allNameSpace) {
                //获取所有pod
                List<V1Pod> podList = v1PodListAll.getItems().stream().filter(item -> item.getMetadata().getNamespace().equals(nameSpace.getSign())).collect(Collectors.toList());
                if (podList == null) {
                    continue;
                }
                //格式化pod
                Map<String, List<V1Pod>> podMap = new HashMap<>();
                if (podList.size() > 0) {
                    for (V1Pod pod : podList) {
                        V1ObjectMeta metadata = pod.getMetadata();
                        if (metadata != null) {
                            Map<String, String> labels = metadata.getLabels();
                            if (labels != null) {
                                List<V1Pod> app = podMap.get(labels.get("app"));
                                if (app == null) {
                                    app = new ArrayList<>();
                                }
                                app.add(pod);
                                if (labels.get("app") != null) {
                                    podMap.put(labels.get("app"), app);
                                }
                            }
                        }
                    }
                }
                //查询应用
                QueryWrapper<App> queryApp = Wrappers.query();
                queryApp.lambda().eq(App::getNamespace, nameSpace.getSign());
                List<AppVO> allApp = JoinUtil.list(appService.list(queryApp), AppVO.class);
                for (AppVO app : allApp) {
                    String state = app.getState();
                    //获取应用pod
                    List<V1Pod> appPodList = podMap.get(app.getSign());
                    if (CollUtil.isEmpty(appPodList)) {
                        if (!"40".equals(state)) {
                            app.setState("40");
                            appService.updateState(app);
                            sendAppState(app);
                        }
                        //修改当前应用部署数
                        if (app.getNowCount() != 0) {
                            app.setNowCount(0);
                            appService.updateNowCount(app);
                            sendAppNowCount(app);
                        }
                    } else {
                        //记录pod记录
                        AppPodRecord appPodRecord = new AppPodRecord();
                        appPodRecord.setAppId(app.getId());
                        List<AppPodRecord> appPodRecordList = appPodRecordService.listAppPodRecord(appPodRecord);

                        //验证pod是否存在
                        for (V1Pod pod : appPodList) {
                            V1ObjectMeta metadata = pod.getMetadata();
                            if (metadata != null && metadata.getName() != null) {
                                String name = metadata.getName();
                                Boolean saveFlag = Boolean.TRUE;
                                if (appPodRecordList != null) {
                                    List<AppPodRecord> podRecordList = appPodRecordList.stream().filter(item -> item.getPodName().equals(name)).collect(Collectors.toList());
                                    if (podRecordList.size() > 0) {
                                        saveFlag = Boolean.FALSE;
                                    }
                                }
                                if (saveFlag) {
                                    TenantContextHolder.setTenantId("'" + nameSpace.getSign() + "'");
                                    AppPodRecord saveAppPodRecord = new AppPodRecord();
                                    saveAppPodRecord.setAppId(app.getId());
                                    saveAppPodRecord.setPodName(name);
                                    appPodRecordService.saveAppPodRecord(saveAppPodRecord);
                                    TenantContextHolder.setTenantId(null);
                                }
                            }
                        }

                        //修改当前应用部署数
                        if (app.getNowCount() == null || app.getNowCount() != appPodList.size()) {
                            app.setNowCount(appPodList.size());
                            appService.updateNowCount(app);
                            sendAppNowCount(app);
                        }
                        //整体pod状态
                        Boolean podStatus = true;
                        //遍历pod
                        for (V1Pod condition : appPodList) {
                            V1PodStatus statusMap = condition.getStatus();
                            if (!KubeConstants.POD_STATE_RUNNING.equals(statusMap.getPhase())) {
                                podStatus = false;
                                if (StrUtil.equals("Evicted", statusMap.getReason())) {
                                    appService.deletePod(condition.getMetadata().getName(), nameSpace.getSign());
                                }
                            }
                        }
                        //pod状态正常
                        if (podStatus) {
                            //当pod状态正常app不是正常的情况将app设置正常
                            if (!"20".equals(state) && !"30".equals(state)) {
                                app.setState("20");
                                appService.updateState(app);
                                sendAppState(app);
                            }
                        } else {
                            //pod状态异常
                            if ("20".equals(state)) {
                                app.setState("60");
                                appService.updateState(app);
                                sendAppState(app);
                                //发送异常消息
                                ErrorRecord errorRecord = new ErrorRecord();
                                errorRecord.setNamespace(nameSpace.getSign());
                                errorRecord.setTitle(app.getName() + "应用异常");
                                errorRecordService.save(errorRecord);
                                //向接收人发送推送
                                pushUserService.sendAbnormalPush(nameSpace.getSign(), app, 0);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("异常:{}", e.getMessage());
        }
    }


    /**
     * 发送pod状态消息
     *
     * @param app
     */
    public void sendAppState(App app) {
        try {
            SockMessage message = new SockMessage();
            message.setNamespace(app.getNamespace());
            Content content = new Content();
            content.setType("appList");
            Map<String, Object> contentMap = new HashMap<>();
            contentMap.put("appId", app.getId());
            contentMap.put("appName", app.getName());
            contentMap.put("state", app.getState());
            content.setContent(contentMap);
            message.setContent(JsonUtils.objectToJson(content));
            webSocketService.send(message);
            log.info("发送应用状态改变通知：{}", message);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("推送websocket消息失败！");
        }
    }

    /**
     * 发送应用当前部署数
     *
     * @param app
     */
    public void sendAppNowCount(App app) {
        SockMessage message = new SockMessage();
        message.setNamespace(app.getNamespace());
        Content content = new Content();
        content.setType("appCount");
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("appId", app.getId());
        contentMap.put("count", app.getNowCount());
        content.setContent(contentMap);
        message.setContent(JsonUtils.objectToJson(content));
        webSocketService.send(message);
    }
}
