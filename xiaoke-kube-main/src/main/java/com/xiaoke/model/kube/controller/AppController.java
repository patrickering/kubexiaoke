package com.xiaoke.model.kube.controller;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.PodLog;
import com.xiaoke.entity.kube.vo.AppVO;
import com.xiaoke.extend.kube.HorizontalPodAutoScalerService;
import com.xiaoke.extend.kube.NodeService;
import com.xiaoke.extend.kube.PodService;
import com.xiaoke.model.kube.service.AppService;
import com.xiaoke.properties.ColonyProperties;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V2beta1HorizontalPodAutoscaler;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用 Controller
 *
 * @author xiaoke
 * @date 2024-08-03 15:34:01
 */
@Api(value = "app", tags = "应用")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app")
public class AppController {

    private final AppService appService;
    private final HorizontalPodAutoScalerService horizontalPodAutoScalerService;
    private final PodService podService;
    private final ColonyProperties colonyProperties;


    @ApiOperation("分页查询应用")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_app_view")
    public R<IPage> page(Page page, App app) {
        return appService.pageApp(page, app);
    }


    @ApiOperation("获取应用列表")
    @GetMapping("/list")
    @Permission("kube_app_view")
    public R list(App app) {
        return R.ok(appService.listApp(app));
    }


    @ApiOperation("通过ID查询应用")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_app_view")
    public R getById(@ApiParam(name = "id", value = "应用ID", required = true) @PathVariable Integer id) {
        AppVO app = appService.getAppById(id);
        List<V1Pod> appPod = appService.getAppPod(app);
        app.setPods(appPod);

        //获取弹性伸缩
        if (app.getAppStretch() != null) {
            V2beta1HorizontalPodAutoscaler horizontalPodAutoScaler = horizontalPodAutoScalerService.getHorizontalPodAutoScaler(app.getSign(), app.getNamespace());
            if (horizontalPodAutoScaler != null) {
                app.getAppStretch().setKubeJson(horizontalPodAutoScaler);
            }
        }
        app.setColonyUrl(colonyProperties.getUrl());
        return R.ok(app);
    }


    @ApiOperation("添加应用")
    @SysLog("添加应用")
    @PostMapping
    @Permission("kube_app_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody AppDTO app, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) throws Exception {
        return appService.saveApp(app, namespace);
    }


    @ApiOperation("修改应用")
    @SysLog("修改应用")
    @PutMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！"), @ValidateFiled(index = 0, notNull = true, maxLen = 100, filedName = "namespace", msg = "[命名空间]验证未通过！"), @ValidateFiled(index = 0, notNull = true, maxLen = 200, filedName = "name", msg = "[名称]验证未通过！"), @ValidateFiled(index = 0, notNull = true, maxLen = 20, filedName = "type", msg = "[应用类型]验证未通过！"), @ValidateFiled(index = 0, maxLen = 10, filedName = "state", msg = "[状态]验证未通过！"), @ValidateFiled(index = 0, maxLen = 200, filedName = "sign", msg = "[标识]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "count", msg = "[部署数量]验证未通过！"), @ValidateFiled(index = 0, notNull = true, maxLen = 255, filedName = "image", msg = "[镜像]验证未通过！"), @ValidateFiled(index = 0, maxLen = 50, filedName = "imageVersion", msg = "[镜像版本]验证未通过！"), @ValidateFiled(index = 0, maxLen = 10, filedName = "memoryRequestCompany", msg = "[内存请求单位]验证未通过！"), @ValidateFiled(index = 0, maxLen = 10, filedName = "memoryLimitCompany", msg = "[内存上限单位]验证未通过！"), @ValidateFiled(index = 0, maxLen = 10, filedName = "updateType", msg = "[更新策略类型]验证未通过！"), @ValidateFiled(index = 0, maxLen = 255, filedName = "dnsType", msg = "[DNS 策略类型]验证未通过！"), @ValidateFiled(index = 0, maxLen = 10, filedName = "healthScheme", msg = "[健康检查协议]验证未通过！"), @ValidateFiled(index = 0, maxLen = 255, filedName = "healthPath", msg = "[健康检查路径]验证未通过！"), @ValidateFiled(index = 0, maxLen = 255, filedName = "remark", msg = "[描述]验证未通过！")})
    @Permission("kube_app_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody AppDTO app) throws Exception {
        return appService.updateApp(app);
    }


    @ApiOperation("删除应用")
    @SysLog("删除应用")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_app_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return appService.removeAppById(id);
    }

    @ApiOperation("停止应用")
    @SysLog("停止应用")
    @DeleteMapping("/stop/{id}")
    @Permission("kube_app_stop")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> stop(@ApiParam(name = "id", value = "ID", required = true) @PathVariable Integer id) throws Exception {
        Boolean stop = appService.stop(id);
        if (stop) {
            return R.ok(Boolean.TRUE, "停止成功！");
        }
        return R.failed(Boolean.FALSE, "停止失败！");
    }

    @ApiOperation("启动应用")
    @SysLog("启动应用")
    @PutMapping("/start/{id}")
    @Permission("kube_app_start")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> start(@ApiParam(name = "id", value = "ID", required = true) @PathVariable Integer id, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) throws Exception {
        Boolean start = appService.start(id, namespace);
        if (start) {
            return R.ok(Boolean.TRUE, "启动成功！");
        }
        return R.failed(Boolean.FALSE, "启动失败！");
    }

    @ApiOperation("重启应用")
    @SysLog("重启应用")
    @GetMapping("/restart/{id}")
    @Permission("kube_app_restart")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> restart(@ApiParam(name = "id", value = "ID", required = true) @PathVariable Integer id) throws Exception {
        AppVO app = appService.getAppById(id);
        if (app == null) {
            return R.failed("重启应用失败，未获取应用信息！");
        }
        //获取应用pod
        List<V1Pod> appPod = appService.getAppPod(app);
        //遍历删除pod
        for (V1Pod pod : appPod) {
            V1ObjectMeta metadata = pod.getMetadata();
            //执行删除pod
            podService.deletePod(metadata.getName(), app.getNamespace());
        }
        app.setState("50");
        appService.updateState(app);
        return R.ok(Boolean.TRUE, "重启应用成功！");
    }

    @ApiOperation("应用伸缩")
    @SysLog("应用伸缩")
    @PutMapping("/telescopic")
    @Permission("kube_app_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> telescopic(@RequestBody AppDTO app) throws Exception {
        //查询应用
        AppVO appVO = appService.getAppById(app.getId());
        if (appVO == null) {
            return R.failed("应用伸缩失败，未获取应用信息！");
        }
        appVO.setCount(app.getCount());
        //执行部署
        AppDTO appDTO = Convert.convert(AppDTO.class, appVO);
        appService.updateApp(appDTO);
        app.setState("70");
        appService.updateState(appVO);
        return R.ok(Boolean.TRUE, "应用伸缩成功！");
    }

    @ApiOperation("应用状态统计")
    @GetMapping("/state-count")
    @Permission("kube_app_view")
    public R<List<Map<String, Object>>> stateCount(App app, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        app.setNamespace(namespace);
        List<Map<String, Object>> stateCountMap = appService.stateCount(app);
        return R.ok(stateCountMap);
    }

    @ApiOperation("获取已使用端口")
    @GetMapping("use-port")
    public R<List<Integer>> usePort(@RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        return R.ok(appService.usePort(namespace));
    }


    @ApiOperation("获取日志")
    @GetMapping("get-log")
    public R<Map> getLog(PodLog podLog, Integer limit, String timestamp, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        String log = podService.podLog(podLog.getName(), podLog.getContainer(), namespace, limit);
        Map<String, Object> map = new HashMap<>();
        if (log == null) {
            map.put("logList", new ArrayList<>());
            map.put("timestamp", timestamp);
            map.put("podName", podLog.getName());
            return R.ok(map);
        }
        List<String> list = new ArrayList<>();
        String[] split = log.split("\n");
        Boolean add = false;
        for (String str : split) {
            String[] s = str.split(" ");
            if (timestamp == null) {
                add = true;
            } else {
                if (!add) {
                    if (timestamp.equals(s[0])) {
                        add = true;
                        continue;
                    }
                }
            }
            if (add) {
                if (s.length > 1) {
                    list.add(str.replaceAll(s[0], ""));
                    timestamp = s[0] + " " + s[1];
                }
            }
        }

        map.put("logList", list);
        map.put("timestamp", timestamp);
        map.put("podName", podLog.getName());
        return R.ok(map);
    }


    @ApiOperation("删除pod")
    @SysLog("删除pod")
    @DeleteMapping("/delete-pod/{podName}")
    @Permission("kube_app_del")
    public R<Boolean> deletePod(@ApiParam(name = "id", value = "ID", required = true) @PathVariable String podName, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        appService.deletePod(podName, namespace);
        return R.ok(Boolean.TRUE, "删除成功！");
    }
}