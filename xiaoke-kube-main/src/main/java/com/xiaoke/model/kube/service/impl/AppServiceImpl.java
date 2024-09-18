package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.*;
import com.xiaoke.entity.kube.vo.AppConfigVO;
import com.xiaoke.entity.kube.vo.AppServiceVO;
import com.xiaoke.entity.kube.vo.AppStorageVO;
import com.xiaoke.entity.kube.vo.AppVO;
import com.xiaoke.entity.system.entity.DictItem;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.extend.kube.DeploymentService;
import com.xiaoke.extend.kube.PodService;
import com.xiaoke.extend.kube.StatefulSetService;
import com.xiaoke.extend.kube.V1ServicesService;
import com.xiaoke.model.kube.mapper.AppImageRecordMapper;
import com.xiaoke.model.kube.mapper.AppMapper;
import com.xiaoke.model.kube.service.AppService;
import com.xiaoke.model.kube.service.*;
import com.xiaoke.model.system.service.DictItemService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 应用 Service
 *
 * @author xiaoke
 * @date 2024-08-03 15:34:01
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {
    private final AppMapper appMapper;
    private final DictItemService dictItemService;
    private final AppPortService appPortService;
    private final AppServiceService appServiceService;
    private final AppConfigService appConfigService;
    private final AppHostService appHostService;
    private final AppStorageService appStorageService;
    private final AppCustomEnvService appCustomEnvService;
    private final StatefulSetService statefulSetService;
    private final DeploymentService deploymentService;
    private final PodService podService;
    private final V1ServicesService v1ServicesService;
    private final AppImageRecordMapper appImageRecordMapper;
    private final AppStretchService appStretchService;

    /**
     * 封装共用查询条件
     *
     * @param app
     * @return
     */
    private QueryWrapper<App> baseQuery(App app) {
        QueryWrapper<App> query = Wrappers.query();
        if (StrUtil.isNotEmpty(app.getNamespace())) {
            query.lambda().eq(App::getNamespace, app.getNamespace());
        }
        if (StrUtil.isNotEmpty(app.getName())) {
            query.lambda().like(App::getName, app.getName());
        }
        if (StrUtil.isNotEmpty(app.getType())) {
            query.lambda().eq(App::getType, app.getType());
        }
        if (StrUtil.isNotEmpty(app.getState())) {
            query.lambda().eq(App::getState, app.getState());
        }
        if (StrUtil.isNotEmpty(app.getSign())) {
            query.lambda().like(App::getSign, app.getSign());
        }
        query.orderByAsc("id");
        return query;
    }


    /**
     * 分页查询应用
     *
     * @param page
     * @param app
     * @return
     */
    @Override
    public R<IPage> pageApp(Page page, App app) {
        QueryWrapper<App> query = this.baseQuery(app);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), AppVO.class));
    }

    /**
     * 获取应用列表
     *
     * @param app
     * @return
     */
    @Override
    public List<AppVO> listApp(App app) {
        QueryWrapper<App> query = this.baseQuery(app);
        if (query == null) {
            return new ArrayList<>();
        }
        return JoinUtil.list(this.list(query), AppVO.class);
    }

    /**
     * 获取应用pod
     *
     * @param app
     */
    @Override
    public List<V1Pod> getAppPod(App app) {
        V1PodList v1PodList = podService.getNameSpacePods(app.getNamespace());
        if (v1PodList == null) {
            return null;
        }
        List<V1Pod> podList = v1PodList.getItems();
        List<V1Pod> pods = new ArrayList<>();
        if (podList != null) {
            for (V1Pod pod : podList) {
                V1ObjectMeta metadata = pod.getMetadata();
                Map<String, String> labels = metadata.getLabels();
                if (labels.get("app").equals(app.getSign())) {
                    if (CollUtil.isNotEmpty(pod.getSpec().getContainers())) {
                        for (V1Container container : pod.getSpec().getContainers()) {
                            if (container.getLivenessProbe() != null && container.getLivenessProbe().getHttpGet() != null) {
                                container.setLivenessProbe(null);
                            }
                        }
                    }
                    pods.add(pod);
                }
            }
        }
        return pods;
    }

    /**
     * 通过ID查询应用
     *
     * @param id
     * @return
     */
    @Override
    public AppVO getAppById(Integer id) {
        return JoinUtil.entity(this.getById(id), AppVO.class);
    }

    /**
     * 添加应用
     *
     * @param app
     * @return
     */
    @Override
    public R<Boolean> saveApp(AppDTO app, String namespace) throws Exception {
        //验证标识是否存在
        QueryWrapper<App> queryCount = Wrappers.query();
        queryCount.lambda().eq(App::getNamespace, namespace);
        queryCount.lambda().eq(App::getSign, app.getSign());
        long count = this.count(queryCount);
        if (count > 0) {
            return R.failed("应用标识已存在！");
        }
        app.setNamespace(namespace);

        SysUser user = SecurityUtils.getUser();
        app.setCreateBy(user.getId());
        app.setUpdateBy(user.getId());
        app.setState("10");
        log.info("添加应用！应用:{}", app);
        Boolean saveFlag = this.save(app);
        if (saveFlag) {
            //添加管理数据
            saveJoin(app);
            createKubeApp(app.getId());
            return R.ok(Boolean.TRUE, "添加应用成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加应用失败！");
        }
    }

    /**
     * 修改应用
     *
     * @param app
     * @return
     */
    @Override
    public R<Boolean> updateApp(AppDTO app) throws Exception {
        SysUser user = SecurityUtils.getUser();
        app.setUpdateBy(user.getId());
        App model = this.getById(app.getId());
        if (!"40".equals(model.getState())) {
            app.setState("70");
        } else {
            app.setState("10");
        }
        log.info("修改应用！id:{}", app.getId());
        Boolean updateFlag = this.updateById(app);
        if (updateFlag) {
            //添加管理数据
            saveJoin(app);
            createKubeApp(app.getId());
            return R.ok(Boolean.TRUE, "修改应用成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改应用失败！");
        }

    }

    /**
     * 保存关联信息
     *
     * @param app
     * @return
     */
    private R<Boolean> saveJoin(AppDTO app) {
        log.info("保存配置文件");
        appConfigService.batchSave(app);
        log.info("保存端口");
        appServiceService.batchSave(app);
        log.info("保存HOST");
        appHostService.batchSave(app);
        log.info("保存存储");
        appStorageService.batchSave(app);
        log.info("保存环境变量");
        if (app.getOpenCustomEnv() != null && !app.getOpenCustomEnv()) {
            app.setCustomEnvList(null);
        }
        appCustomEnvService.batchSave(app);
        return R.ok(Boolean.TRUE, "保存成功");
    }

    /**
     * 创建Kubernetes应用
     * <p>
     * 此方法负责在Kubernetes中创建应用它首先从数据库获取应用的详细信息，
     * 然后根据应用的配置创建相应的Kubernetes资源
     *
     * @param appId 应用ID，用于从数据库中检索应用信息
     * @return 返回创建操作是否成功
     */
    @Override
    public void createKubeApp(Integer appId) throws Exception {
        // 通过ID获取应用实体
        App app = this.getById(appId);
        // 将应用实体转换为AppVO视图对象
        AppVO model = JoinUtil.entity(app, AppVO.class);

        if ("StatefulSet".equals(model.getType())) {
            V1StatefulSet statefulSetApp = createStatefulSetApp(model);
            if (statefulSetApp == null) {
                throw new Exception("创建应用失败！");
            }
        } else {
            V1Deployment deploymentApp = createDeploymentApp(model);
            if (deploymentApp == null) {
                throw new Exception("创建应用失败！");
            }
        }
        //创建Service
        createService(model);

        //保存镜像更新记录
        QueryWrapper<AppImageRecord> queryAppImageRecord = Wrappers.query();
        queryAppImageRecord.lambda().eq(AppImageRecord::getAppId, model.getId());
        queryAppImageRecord.orderByDesc("id");
        queryAppImageRecord.last(" LIMIT 1");
        //获取最后一个镜像
        AppImageRecord appImageRecord = appImageRecordMapper.selectOne(queryAppImageRecord);
        SysUser user = SecurityUtils.getUser();
        AppImageRecord saveAppImageRecord = new AppImageRecord();
        saveAppImageRecord.setAppId(model.getId());
        if (user == null) {
            saveAppImageRecord.setCreateBy(0);
        } else {
            saveAppImageRecord.setCreateBy(user.getId());
        }
        if (appImageRecord == null) {
            saveAppImageRecord.setImage(model.getImage());
            saveAppImageRecord.setImageVersion(model.getImageVersion());
            appImageRecordMapper.insert(saveAppImageRecord);
        } else {
            if (!StrUtil.equals(appImageRecord.getImage(), model.getImage()) || !StrUtil.equals(appImageRecord.getImageVersion(), model.getImageVersion())) {
                saveAppImageRecord.setImage(model.getImage());
                saveAppImageRecord.setImageVersion(model.getImageVersion());
                appImageRecordMapper.insert(saveAppImageRecord);
            }
        }

    }

    /**
     * 创建有状态应用
     *
     * @param app
     */
    V1StatefulSet createStatefulSetApp(AppVO app) {
        V1StatefulSet statefulSet = new V1StatefulSet();
        statefulSet.setKind(app.getType());
        statefulSet.setApiVersion("apps/v1");

        V1ObjectMeta statefulSetMetadata = new V1ObjectMeta();
        statefulSetMetadata.setName(app.getSign());
        Map<String, String> statefulSetMetadataLabels = new HashMap<>();
        statefulSetMetadataLabels.put("app", app.getSign());
        statefulSetMetadata.setLabels(statefulSetMetadataLabels);
        statefulSet.setMetadata(statefulSetMetadata);

        V1StatefulSetSpec statefulSetSpec = new V1StatefulSetSpec();

        statefulSetSpec.setReplicas(app.getCount());

        V1LabelSelector statefulSetSpecSelector = new V1LabelSelector();
        Map<String, String> statefulSetSpecSelectorMatchLabels = new HashMap<>();
        statefulSetSpecSelectorMatchLabels.put("app", app.getSign());
        statefulSetSpecSelector.setMatchLabels(statefulSetSpecSelectorMatchLabels);
        statefulSetSpec.setSelector(statefulSetSpecSelector);

        V1PodTemplateSpec statefulSetSpecTemplate = new V1PodTemplateSpec();
        V1ObjectMeta statefulSetSpecTemplateMetadata = new V1ObjectMeta();
        Map<String, String> statefulSetSpecTemplateMetadataLabels = new HashMap<>();
        statefulSetSpecTemplateMetadataLabels.put("app", app.getSign());
        statefulSetSpecTemplateMetadata.setLabels(statefulSetSpecTemplateMetadataLabels);
        V1PodSpec statefulSetSpecTemplateSpec = new V1PodSpec();
        List<V1Container> statefulSetSpecTemplateSpecContainerList = new ArrayList<>();
        V1Container statefulSetSpecTemplateSpecContainer = new V1Container();
        statefulSetSpecTemplateSpecContainer.setName(app.getSign());
        statefulSetSpecTemplateSpecContainer.setImage(app.getImage() + ":" + app.getImageVersion());
        V1ResourceRequirements statefulSetSpecTemplateSpecContainerResources = new V1ResourceRequirements();
        Map<String, Quantity> statefulSetSpecTemplateSpecContainerResourceLimit = new HashMap<>();
        statefulSetSpecTemplateSpecContainerResourceLimit.put("cpu", new Quantity(Double.valueOf(app.getCpuLimit() * 1000).toString() + "m"));
        statefulSetSpecTemplateSpecContainerResourceLimit.put("memory", new Quantity(Integer.valueOf(app.getMemoryLimit()).toString() + app.getMemoryLimitCompany()));

        statefulSetSpecTemplateSpecContainerResources.setLimits(statefulSetSpecTemplateSpecContainerResourceLimit);
        Map<String, Quantity> statefulSetSpecTemplateSpecContainerResourceRequest = new HashMap<>();
        statefulSetSpecTemplateSpecContainerResourceRequest.put("cpu", new Quantity(Double.valueOf(app.getCpuRequest() * 1000).toString() + "m"));
        statefulSetSpecTemplateSpecContainerResourceRequest.put("memory", new Quantity(Integer.valueOf(app.getMemoryRequest()).toString() + app.getMemoryRequestCompany()));

        statefulSetSpecTemplateSpecContainerResources.setRequests(statefulSetSpecTemplateSpecContainerResourceRequest);
        statefulSetSpecTemplateSpecContainer.setResources(statefulSetSpecTemplateSpecContainerResources);
        List<V1ContainerPort> statefulSetSpecTemplateSpecContainerPortList = new ArrayList<>();

        List<AppServiceVO> serviceList = app.getServiceList();
        for (AppServiceVO appService : serviceList) {
            if ("Service".equals(appService.getType())) {
                List<AppPort> portList = appService.getPortList();
                for (AppPort port : portList) {
                    V1ContainerPort statefulSetSpecTemplateSpecContainerPort = new V1ContainerPort();
                    statefulSetSpecTemplateSpecContainerPort.setContainerPort(port.getContainer());
                    statefulSetSpecTemplateSpecContainerPortList.add(statefulSetSpecTemplateSpecContainerPort);
                }
            } else {
                statefulSetSpec.setServiceName(appService.getSign());
            }
        }
        if (statefulSetSpecTemplateSpecContainerPortList.size() > 0) {
            statefulSetSpecTemplateSpecContainer.setPorts(statefulSetSpecTemplateSpecContainerPortList);
        }

        if (app.getOpenEnvConfig() != null && app.getOpenEnvConfig()) {
            List<AppConfigVO> configList = app.getConfigList();
            List<V1EnvFromSource> statefulSetSpecTemplateSpecContainerEnvFromList = new ArrayList<>();
            if (configList != null && configList.size() > 0) {
                for (AppConfigVO configApp : configList) {
                    if ("0".equals(configApp.getConfig().getType())) {
                        V1EnvFromSource statefulSetSpecTemplateSpecContainerEnvFrom = new V1EnvFromSource();
                        String configName = configApp.getConfig().getName();
                        Integer version = configApp.getVersion().getVersion();
                        V1ConfigMapEnvSource statefulSetSpecTemplateSpecContainerEnvFromConfigMapRef = new V1ConfigMapEnvSource();
                        statefulSetSpecTemplateSpecContainerEnvFromConfigMapRef.setName(configName + ".v" + version);
                        statefulSetSpecTemplateSpecContainerEnvFrom.setConfigMapRef(statefulSetSpecTemplateSpecContainerEnvFromConfigMapRef);
                        statefulSetSpecTemplateSpecContainerEnvFromList.add(statefulSetSpecTemplateSpecContainerEnvFrom);
                    }
                }
            }
            if (statefulSetSpecTemplateSpecContainerEnvFromList != null && statefulSetSpecTemplateSpecContainerEnvFromList.size() > 0) {
                statefulSetSpecTemplateSpecContainer.setEnvFrom(statefulSetSpecTemplateSpecContainerEnvFromList);
            }
        }

        //自定义环境变量
        if (app.getOpenCustomEnv() != null && app.getOpenCustomEnv()) {
            List<AppCustomEnv> customEnvList = app.getCustomEnvList();
            if (customEnvList != null && customEnvList.size() > 0) {
                List<V1EnvVar> statefulSetSpecTemplateSpecContainerEnvList = new ArrayList<>();
                for (AppCustomEnv customEnv : customEnvList) {
                    V1EnvVar statefulSetSpecTemplateSpecContainerEnv = new V1EnvVar();
                    statefulSetSpecTemplateSpecContainerEnv.setName(customEnv.getEnvKey());
                    statefulSetSpecTemplateSpecContainerEnv.setValue(customEnv.getEnvKey());
                    statefulSetSpecTemplateSpecContainerEnvList.add(statefulSetSpecTemplateSpecContainerEnv);
                }
                statefulSetSpecTemplateSpecContainer.setEnv(statefulSetSpecTemplateSpecContainerEnvList);
            }
        }

        //挂载配置文件
        List<V1VolumeMount> statefulSetSpecTemplateSpecContainerVolumeMountsList = new ArrayList<>();
        if (app.getOpenConfig()) {
            List<AppConfigVO> configList = app.getConfigList();
            Integer index = 0;
            for (AppConfigVO configApp : configList) {
                if ("1".equals(configApp.getConfig().getType())) {
                    String configName = configApp.getConfig().getName();
                    Integer version = configApp.getVersion().getVersion();
                    V1VolumeMount statefulSetSpecTemplateSpecContainerVolumeMounts = new V1VolumeMount();
                    statefulSetSpecTemplateSpecContainerVolumeMounts.setMountPath(configApp.getCatalog() + "/" + configApp.getFileName());
                    statefulSetSpecTemplateSpecContainerVolumeMounts.setName(configName + "v" + (version * 100 + index));
                    statefulSetSpecTemplateSpecContainerVolumeMounts.setReadOnly(true);
                    statefulSetSpecTemplateSpecContainerVolumeMounts.setSubPath(configApp.getFileName());
                    index++;
                    statefulSetSpecTemplateSpecContainerVolumeMountsList.add(statefulSetSpecTemplateSpecContainerVolumeMounts);
                }

            }
        }


        //存储
        if ("StatefulSet".equals(app.getType())) {
            List<AppStorageVO> storageList = app.getStorageList();
            if (storageList != null && storageList.size() > 0) {
                //有状态服务存储
                List<V1PersistentVolumeClaim> statefulSetSpecVolumeClaimTemplatesList = new ArrayList<>();
                for (AppStorageVO storageApp : storageList) {
                    V1PersistentVolumeClaim statefulSetSpecVolumeClaimTemplates = new V1PersistentVolumeClaim();
                    V1ObjectMeta statefulSetSpecVolumeClaimTemplatesMetada = new V1ObjectMeta();
                    statefulSetSpecVolumeClaimTemplatesMetada.setName(storageApp.getName());
                    statefulSetSpecVolumeClaimTemplates.setMetadata(statefulSetSpecVolumeClaimTemplatesMetada);

                    V1PersistentVolumeClaimSpec statefulSetSpecVolumeClaimTemplatesSpec = new V1PersistentVolumeClaimSpec();
                    List<String> accessModesList = new ArrayList<>();
                    accessModesList.add("ReadWriteOnce");
                    statefulSetSpecVolumeClaimTemplatesSpec.setAccessModes(accessModesList);
                    statefulSetSpecVolumeClaimTemplatesSpec.setStorageClassName("managed-nfs-storage");
                    V1ResourceRequirements statefulSetSpecVolumeClaimTemplatesSpecResources = new V1ResourceRequirements();
                    Map<String, Quantity> statefulSetSpecVolumeClaimTemplatesSpecResourcesRequests = new HashMap<>();

                    statefulSetSpecVolumeClaimTemplatesSpecResourcesRequests.put("storage", new Quantity(storageApp.getCapacity() + "Gi"));
                    statefulSetSpecVolumeClaimTemplatesSpecResources.setRequests(statefulSetSpecVolumeClaimTemplatesSpecResourcesRequests);
                    statefulSetSpecVolumeClaimTemplatesSpec.setResources(statefulSetSpecVolumeClaimTemplatesSpecResources);
                    statefulSetSpecVolumeClaimTemplates.setSpec(statefulSetSpecVolumeClaimTemplatesSpec);
                    statefulSetSpecVolumeClaimTemplatesList.add(statefulSetSpecVolumeClaimTemplates);
                }
                statefulSetSpec.setVolumeClaimTemplates(statefulSetSpecVolumeClaimTemplatesList);
            }
        }


        List<AppStorageVO> storageList = app.getStorageList();
        if (CollectionUtil.isNotEmpty(storageList)) {
            for (AppStorageVO storageApp : storageList) {
                V1VolumeMount statefulSetSpecTemplateSpecContainerVolumeMounts = new V1VolumeMount();
                statefulSetSpecTemplateSpecContainerVolumeMounts.setMountPath(storageApp.getPath());
                statefulSetSpecTemplateSpecContainerVolumeMounts.setName(storageApp.getName());
                statefulSetSpecTemplateSpecContainerVolumeMountsList.add(statefulSetSpecTemplateSpecContainerVolumeMounts);
            }
        }

        if (statefulSetSpecTemplateSpecContainerVolumeMountsList != null && statefulSetSpecTemplateSpecContainerVolumeMountsList.size() > 0) {
            statefulSetSpecTemplateSpecContainer.setVolumeMounts(statefulSetSpecTemplateSpecContainerVolumeMountsList);
        }


        statefulSetSpecTemplateSpecContainerList.add(statefulSetSpecTemplateSpecContainer);
        statefulSetSpecTemplateSpec.setContainers(statefulSetSpecTemplateSpecContainerList);

        //设置dns策略
        statefulSetSpecTemplateSpec.setDnsPolicy(app.getDnsType());
        //host
        List<AppHost> hostList = app.getHostList();
        if (CollectionUtil.isNotEmpty(hostList)) {
            List<V1HostAlias> statefulSetSpecTemplateSpecHostAliasesList = new ArrayList();
            for (AppHost host : hostList) {
                V1HostAlias statefulSetSpecTemplateSpecHostAliases = new V1HostAlias();
                statefulSetSpecTemplateSpecHostAliases.setIp(host.getIp());
                List<String> statefulSetSpecTemplateSpecHostAliasesHostnames = new ArrayList<>();
                statefulSetSpecTemplateSpecHostAliasesHostnames.add(host.getDomain());
                statefulSetSpecTemplateSpecHostAliases.setHostnames(statefulSetSpecTemplateSpecHostAliasesHostnames);
                statefulSetSpecTemplateSpecHostAliasesList.add(statefulSetSpecTemplateSpecHostAliases);
            }
            statefulSetSpecTemplateSpec.setHostAliases(statefulSetSpecTemplateSpecHostAliasesList);
        }


        //挂载配置
        List<V1Volume> statefulSetSpecTemplateSpecVolumesList = new ArrayList<>();
        if (app.getOpenConfig()) {
            List<AppConfigVO> configList = app.getConfigList();
            Integer index = 0;
            for (AppConfigVO appConfig : configList) {
                if ("1".equals(appConfig.getConfig().getType())) {
                    //配置文件名
                    String configName = appConfig.getConfig().getName();
                    Integer version = appConfig.getVersion().getVersion();
                    V1Volume statefulSetSpecTemplateSpecVolumes = new V1Volume();
                    V1ConfigMapVolumeSource statefulSetSpecTemplateSpecVolumesConfigMap = new V1ConfigMapVolumeSource();
                    statefulSetSpecTemplateSpecVolumesConfigMap.setDefaultMode(0777);
                    statefulSetSpecTemplateSpecVolumesConfigMap.setName(configName + ".v" + version);

                    List<V1KeyToPath> statefulSetSpecTemplateSpecVolumesConfigMapConfigMapItemsList = new ArrayList<>();
                    V1KeyToPath statefulSetSpecVolumesConfigMapConfigMapItems = new V1KeyToPath();
                    statefulSetSpecVolumesConfigMapConfigMapItems.setKey(appConfig.getConfig().getName());
                    statefulSetSpecVolumesConfigMapConfigMapItems.setMode(0777);
                    statefulSetSpecVolumesConfigMapConfigMapItems.setPath(appConfig.getFileName());
                    statefulSetSpecTemplateSpecVolumesConfigMapConfigMapItemsList.add(statefulSetSpecVolumesConfigMapConfigMapItems);
                    statefulSetSpecTemplateSpecVolumesConfigMap.setItems(statefulSetSpecTemplateSpecVolumesConfigMapConfigMapItemsList);
                    statefulSetSpecTemplateSpecVolumes.setName(configName + "v" + (version * 100 + index));
                    statefulSetSpecTemplateSpecVolumes.setConfigMap(statefulSetSpecTemplateSpecVolumesConfigMap);
                    index++;
                    statefulSetSpecTemplateSpecVolumesList.add(statefulSetSpecTemplateSpecVolumes);
                }
            }
        }


        if (CollectionUtil.isNotEmpty(storageList)) {
            Integer index = 0;
            for (AppStorageVO storageApp : storageList) {
                if (storageApp.getStorage() != null) {
                    V1Volume statefulSetSpecTemplateSpecVolumes = new V1Volume();
                    statefulSetSpecTemplateSpecVolumes.setName("volume-" + index);
                    V1PersistentVolumeClaimVolumeSource statefulSetSpecTemplateSpecVolumesPersistentVolumeClaim = new V1PersistentVolumeClaimVolumeSource();
                    statefulSetSpecTemplateSpecVolumesPersistentVolumeClaim.setClaimName(app.getNamespace() + "-" + storageApp.getStorage().getName());
                    statefulSetSpecTemplateSpecVolumes.setPersistentVolumeClaim(statefulSetSpecTemplateSpecVolumesPersistentVolumeClaim);
                    statefulSetSpecTemplateSpecVolumesList.add(statefulSetSpecTemplateSpecVolumes);
                    index++;
                }
            }
        }

        if (statefulSetSpecTemplateSpecVolumesList.size() > 0) {
            statefulSetSpecTemplateSpec.setVolumes(statefulSetSpecTemplateSpecVolumesList);
        }
        statefulSetSpecTemplate.setMetadata(statefulSetSpecTemplateMetadata);
        statefulSetSpecTemplate.setSpec(statefulSetSpecTemplateSpec);
        statefulSetSpec.setTemplate(statefulSetSpecTemplate);
        statefulSet.setSpec(statefulSetSpec);

        V1StatefulSet statefulSetTemp = statefulSetService.getStatefulSet(app.getSign(), app.getNamespace());
        if (statefulSetTemp != null) {
            return statefulSetService.updateStatefulSet(statefulSet, app.getSign(), app.getNamespace());
        } else {
            return statefulSetService.createStatefulSet(statefulSet, app.getNamespace());
        }
    }

    /**
     * 创建无状态应用
     *
     * @param app
     */
    V1Deployment createDeploymentApp(AppVO app) {
        V1Deployment deployment = new V1Deployment();

        deployment.setKind(app.getType());
        deployment.setApiVersion("apps/v1");

        V1ObjectMeta deploymentMetadata = new V1ObjectMeta();
        deploymentMetadata.setName(app.getSign());
        Map<String, String> deploymentMetadataLabels = new HashMap<>();
        deploymentMetadataLabels.put("app", app.getSign());
        deploymentMetadata.setLabels(deploymentMetadataLabels);
        deployment.setMetadata(deploymentMetadata);

        V1DeploymentSpec deploymentSpec = new V1DeploymentSpec();

        deploymentSpec.setReplicas(app.getCount());

        V1LabelSelector deploymentSpecSelector = new V1LabelSelector();
        Map<String, String> deploymentSpecSelectorMatchLabels = new HashMap<>();
        deploymentSpecSelectorMatchLabels.put("app", app.getSign());
        deploymentSpecSelector.setMatchLabels(deploymentSpecSelectorMatchLabels);
        deploymentSpec.setSelector(deploymentSpecSelector);

        V1PodTemplateSpec deploymentSpecTemplate = new V1PodTemplateSpec();
        V1ObjectMeta deploymentSpecTemplateMetadata = new V1ObjectMeta();
        Map<String, String> deploymentSpecTemplateMetadataLabels = new HashMap<>();
        deploymentSpecTemplateMetadataLabels.put("app", app.getSign());
        deploymentSpecTemplateMetadata.setLabels(deploymentSpecTemplateMetadataLabels);
        V1PodSpec deploymentSpecTemplateSpec = new V1PodSpec();

       /* Map<String, String> deploymentSpecTemplateSpecNodeSelector = new HashMap<>();
        deploymentSpecTemplateSpecNodeSelector.put("application", "true");
        deploymentSpecTemplateSpec.setNodeSelector(deploymentSpecTemplateSpecNodeSelector);*/

        List<V1Container> deploymentSpecTemplateSpecContainerList = new ArrayList<>();
        V1Container deploymentSpecTemplateSpecContainer = new V1Container();
        deploymentSpecTemplateSpecContainer.setName(app.getSign());
        deploymentSpecTemplateSpecContainer.setImage(app.getImage() + ":" + app.getImageVersion());


        if (app.getOpenHealth() != null && app.getOpenHealth()) {
            V1Probe deploymentSpecTemplateSpecContainerLivenessProbe = new V1Probe();
            //失败次数
            deploymentSpecTemplateSpecContainerLivenessProbe.failureThreshold(app.getFailureThreshold());
            //启动容器后进行首次健康检查的等待时间
            deploymentSpecTemplateSpecContainerLivenessProbe.setInitialDelaySeconds(app.getInitialDelay());
            //探针的探测周期
            deploymentSpecTemplateSpecContainerLivenessProbe.setPeriodSeconds(app.getPeriodSeconds());
            //探针的成功的阈值
            deploymentSpecTemplateSpecContainerLivenessProbe.setSuccessThreshold(app.getSuccessThreshold());
            //探测的超时的秒数
            deploymentSpecTemplateSpecContainerLivenessProbe.setTimeoutSeconds(app.getTimeoutSeconds());
            V1HTTPGetAction deploymentSpecTemplateSpecContainerLivenessProbeHttpGet = new V1HTTPGetAction();
            deploymentSpecTemplateSpecContainerLivenessProbeHttpGet.setPath(app.getHealthPath());
            deploymentSpecTemplateSpecContainerLivenessProbeHttpGet.setPort(new IntOrString(app.getHealthPort()));
            deploymentSpecTemplateSpecContainerLivenessProbeHttpGet.setScheme(app.getHealthScheme());
            deploymentSpecTemplateSpecContainerLivenessProbe.setHttpGet(deploymentSpecTemplateSpecContainerLivenessProbeHttpGet);
            deploymentSpecTemplateSpecContainer.setLivenessProbe(deploymentSpecTemplateSpecContainerLivenessProbe);
        }


        V1ResourceRequirements deploymentSpecTemplateSpecContainerResources = new V1ResourceRequirements();
        Map<String, Quantity> deploymentSpecTemplateSpecContainerResourceLimit = new HashMap<>();

        deploymentSpecTemplateSpecContainerResourceLimit.put("cpu", new Quantity(Double.valueOf(app.getCpuLimit() * 1000).toString() + "m"));

        deploymentSpecTemplateSpecContainerResourceLimit.put("memory", new Quantity(Integer.valueOf(app.getMemoryLimit()).toString() + app.getMemoryLimitCompany()));

        deploymentSpecTemplateSpecContainerResources.setLimits(deploymentSpecTemplateSpecContainerResourceLimit);
        Map<String, Quantity> deploymentSpecTemplateSpecContainerResourceRequest = new HashMap<>();
        deploymentSpecTemplateSpecContainerResourceRequest.put("cpu", new Quantity(Double.valueOf(app.getCpuRequest() * 1000).toString() + "m"));
        deploymentSpecTemplateSpecContainerResourceRequest.put("memory", new Quantity(Integer.valueOf(app.getMemoryRequest()).toString() + app.getMemoryRequestCompany()));

        deploymentSpecTemplateSpecContainerResources.setRequests(deploymentSpecTemplateSpecContainerResourceRequest);
        deploymentSpecTemplateSpecContainer.setResources(deploymentSpecTemplateSpecContainerResources);
        List<V1ContainerPort> deploymentSpecTemplateSpecContainerPortList = new ArrayList<>();

        List<AppServiceVO> serviceList = app.getServiceList();
        if (CollectionUtil.isNotEmpty(serviceList)) {
            for (AppServiceVO appServiceEntity : serviceList) {
                if ("Service".equals(appServiceEntity.getType())) {
                    List<AppPort> portList = appServiceEntity.getPortList();
                    if (CollectionUtil.isNotEmpty(portList)) {
                        for (AppPort port : portList) {
                            V1ContainerPort deploymentSpecTemplateSpecContainerPort = new V1ContainerPort();
                            deploymentSpecTemplateSpecContainerPort.setContainerPort(port.getContainer());
                            deploymentSpecTemplateSpecContainerPortList.add(deploymentSpecTemplateSpecContainerPort);
                        }
                    }

                }
            }
        }

        if (deploymentSpecTemplateSpecContainerPortList.size() > 0) {
            deploymentSpecTemplateSpecContainer.setPorts(deploymentSpecTemplateSpecContainerPortList);
        }

        if (app.getOpenEnvConfig() != null && app.getOpenEnvConfig()) {
            List<AppConfigVO> configList = app.getConfigList();
            List<V1EnvFromSource> deploymentSpecTemplateSpecContainerEnvFromList = new ArrayList<>();
            if (configList != null && configList.size() > 0) {
                for (AppConfigVO appConfig : configList) {
                    if ("0".equals(appConfig.getConfig().getType())) {
                        V1EnvFromSource deploymentSpecTemplateSpecContainerEnvFrom = new V1EnvFromSource();
                        String configName = appConfig.getConfig().getName();
                        Integer version = appConfig.getVersion().getVersion();
                        V1ConfigMapEnvSource deploymentSpecTemplateSpecContainerEnvFromConfigMapRef = new V1ConfigMapEnvSource();
                        deploymentSpecTemplateSpecContainerEnvFromConfigMapRef.setName(configName + ".v" + version);
                        deploymentSpecTemplateSpecContainerEnvFrom.setConfigMapRef(deploymentSpecTemplateSpecContainerEnvFromConfigMapRef);
                        deploymentSpecTemplateSpecContainerEnvFromList.add(deploymentSpecTemplateSpecContainerEnvFrom);
                    }
                }
            }
            if (deploymentSpecTemplateSpecContainerEnvFromList != null && deploymentSpecTemplateSpecContainerEnvFromList.size() > 0) {
                deploymentSpecTemplateSpecContainer.setEnvFrom(deploymentSpecTemplateSpecContainerEnvFromList);
            }
        }

        //自定义环境变量
        if (app.getOpenCustomEnv() != null && app.getOpenCustomEnv()) {
            List<AppCustomEnv> customEnvList = app.getCustomEnvList();
            if (CollUtil.isNotEmpty(customEnvList)) {
                List<V1EnvVar> deploymentSpecTemplateSpecContainerEnvList = new ArrayList<>();
                for (AppCustomEnv customEnv : customEnvList) {
                    V1EnvVar deploymentSpecTemplateSpecContainerEnv = new V1EnvVar();
                    deploymentSpecTemplateSpecContainerEnv.setName(customEnv.getEnvKey());
                    deploymentSpecTemplateSpecContainerEnv.setValue(customEnv.getEnvValue());
                    deploymentSpecTemplateSpecContainerEnvList.add(deploymentSpecTemplateSpecContainerEnv);
                }
                deploymentSpecTemplateSpecContainer.setEnv(deploymentSpecTemplateSpecContainerEnvList);
            }
        }

        //挂载配置文件
        List<V1VolumeMount> deploymentSpecTemplateSpecContainerVolumeMountsList = new ArrayList<>();
        if (app.getOpenConfig()) {
            List<AppConfigVO> configList = app.getConfigList();
            Integer index = 0;
            for (AppConfigVO configApp : configList) {
                if ("1".equals(configApp.getVersion().getConfig().getType())) {
                    String configName = configApp.getVersion().getConfig().getName();
                    Integer version = configApp.getVersion().getVersion();
                    V1VolumeMount deploymentSpecTemplateSpecContainerVolumeMounts = new V1VolumeMount();
                    deploymentSpecTemplateSpecContainerVolumeMounts.setMountPath(configApp.getCatalog() + "/" + configApp.getFileName());
                    deploymentSpecTemplateSpecContainerVolumeMounts.setName(configName + "v" + (version * 100 + index));
                    deploymentSpecTemplateSpecContainerVolumeMounts.setReadOnly(true);
                    deploymentSpecTemplateSpecContainerVolumeMounts.setSubPath(configApp.getFileName());
                    index++;
                    deploymentSpecTemplateSpecContainerVolumeMountsList.add(deploymentSpecTemplateSpecContainerVolumeMounts);
                }

            }
        }

        List<AppStorageVO> storageList = app.getStorageList();
        if (CollectionUtil.isNotEmpty(storageList)) {
            Integer index = 0;
            for (AppStorageVO storageApp : storageList) {
                if (storageApp.getStorage() != null) {
                    V1VolumeMount deploymentSpecTemplateSpecContainerVolumeMounts = new V1VolumeMount();
                    deploymentSpecTemplateSpecContainerVolumeMounts.setMountPath(storageApp.getPath());
                    deploymentSpecTemplateSpecContainerVolumeMounts.setName("volume-" + index);
                    index++;
                    deploymentSpecTemplateSpecContainerVolumeMountsList.add(deploymentSpecTemplateSpecContainerVolumeMounts);
                }
            }
        }

        if (deploymentSpecTemplateSpecContainerVolumeMountsList != null && deploymentSpecTemplateSpecContainerVolumeMountsList.size() > 0) {
            deploymentSpecTemplateSpecContainer.setVolumeMounts(deploymentSpecTemplateSpecContainerVolumeMountsList);
        }


        deploymentSpecTemplateSpecContainerList.add(deploymentSpecTemplateSpecContainer);
        deploymentSpecTemplateSpec.setContainers(deploymentSpecTemplateSpecContainerList);

        //设置dns策略
        deploymentSpecTemplateSpec.setDnsPolicy(app.getDnsType());
        //host
        List<AppHost> hostList = app.getHostList();
        if (hostList != null && hostList.size() > 0) {
            List<V1HostAlias> deploymentSpecTemplateSpecHostAliasesList = new ArrayList();
            for (AppHost host : hostList) {
                V1HostAlias deploymentSpecTemplateSpecHostAliases = new V1HostAlias();
                deploymentSpecTemplateSpecHostAliases.setIp(host.getIp());
                List<String> deploymentSpecTemplateSpecHostAliasesHostnames = new ArrayList<>();
                deploymentSpecTemplateSpecHostAliasesHostnames.add(host.getDomain());
                deploymentSpecTemplateSpecHostAliases.setHostnames(deploymentSpecTemplateSpecHostAliasesHostnames);
                deploymentSpecTemplateSpecHostAliasesList.add(deploymentSpecTemplateSpecHostAliases);
            }
            deploymentSpecTemplateSpec.setHostAliases(deploymentSpecTemplateSpecHostAliasesList);
        }


        //挂载配置
        List<V1Volume> deploymentSpecTemplateSpecVolumesList = new ArrayList<>();
        if (app.getOpenConfig()) {
            List<AppConfigVO> configList = app.getConfigList();
            Integer index = 0;
            for (AppConfigVO configApp : configList) {
                if ("1".equals(configApp.getVersion().getConfig().getType())) {
                    //配置文件名
                    String configName = configApp.getVersion().getConfig().getName();
                    Integer version = configApp.getVersion().getVersion();
                    V1Volume deploymentSpecTemplateSpecVolumes = new V1Volume();
                    V1ConfigMapVolumeSource deploymentSpecTemplateSpecVolumesConfigMap = new V1ConfigMapVolumeSource();
                    deploymentSpecTemplateSpecVolumesConfigMap.setDefaultMode(0777);
                    deploymentSpecTemplateSpecVolumesConfigMap.setName(configName + ".v" + version);

                    List<V1KeyToPath> deploymentSpecTemplateSpecVolumesConfigMapConfigMapItemsList = new ArrayList<>();
                    V1KeyToPath deploymentSpecVolumesConfigMapConfigMapItems = new V1KeyToPath();
                    deploymentSpecVolumesConfigMapConfigMapItems.setKey(configName);
                    deploymentSpecVolumesConfigMapConfigMapItems.setMode(0777);
                    deploymentSpecVolumesConfigMapConfigMapItems.setPath(configApp.getFileName());
                    deploymentSpecTemplateSpecVolumesConfigMapConfigMapItemsList.add(deploymentSpecVolumesConfigMapConfigMapItems);
                    deploymentSpecTemplateSpecVolumesConfigMap.setItems(deploymentSpecTemplateSpecVolumesConfigMapConfigMapItemsList);
                    deploymentSpecTemplateSpecVolumes.setName(configName + "v" + (version * 100 + index));
                    deploymentSpecTemplateSpecVolumes.setConfigMap(deploymentSpecTemplateSpecVolumesConfigMap);
                    index++;
                    deploymentSpecTemplateSpecVolumesList.add(deploymentSpecTemplateSpecVolumes);
                }
            }
        }
        if (CollectionUtil.isNotEmpty(storageList)) {
            Integer index = 0;
            for (AppStorageVO storageApp : storageList) {
                if (storageApp.getStorage() != null) {
                    V1Volume deploymentSpecTemplateSpecVolumes = new V1Volume();
                    deploymentSpecTemplateSpecVolumes.setName("volume-" + index);
                    V1PersistentVolumeClaimVolumeSource deploymentSpecTemplateSpecVolumesPersistentVolumeClaim = new V1PersistentVolumeClaimVolumeSource();
                    deploymentSpecTemplateSpecVolumesPersistentVolumeClaim.setClaimName(app.getNamespace() + "-" + storageApp.getStorage().getName());
                    deploymentSpecTemplateSpecVolumes.setPersistentVolumeClaim(deploymentSpecTemplateSpecVolumesPersistentVolumeClaim);
                    deploymentSpecTemplateSpecVolumesList.add(deploymentSpecTemplateSpecVolumes);
                    index++;
                }
            }
        }

        if (deploymentSpecTemplateSpecVolumesList.size() > 0) {
            deploymentSpecTemplateSpec.setVolumes(deploymentSpecTemplateSpecVolumesList);
        }

        deploymentSpecTemplate.setMetadata(deploymentSpecTemplateMetadata);
        deploymentSpecTemplate.setSpec(deploymentSpecTemplateSpec);
        deploymentSpec.setTemplate(deploymentSpecTemplate);

        deployment.setSpec(deploymentSpec);

        V1Deployment deploymentTemp = deploymentService.getDeployment(app.getSign(), app.getNamespace());

        if (deploymentTemp != null) {
            return deploymentService.updateDeployment(deployment, app.getSign(), app.getNamespace());
        } else {
            return deploymentService.createDeployment(deployment, app.getNamespace());

        }
    }

    /**
     * 创建Service
     *
     * @param model
     * @return
     */
    private Boolean createService(AppVO model) {
        try {
            //新Service列表
            List<AppServiceVO> serviceList = model.getServiceList();

            //待删除Service
            List<com.xiaoke.entity.kube.entity.AppService> deleteServiceList = new ArrayList<>();
            if (CollUtil.isNotEmpty(model.getServiceList())) {
                for (AppServiceVO appService : model.getServiceList()) {
                    if (appService.getId() != null) {
                        deleteServiceList.add(appService);
                    }
                }
            }

            if (serviceList != null && serviceList.size() > 0) {
                for (AppServiceVO appServiceEntity : serviceList) {
                    Boolean flag = Boolean.TRUE;
                    com.xiaoke.entity.kube.entity.AppService tempServiceEntity = null;
                    if (appServiceEntity.getId() != null) {
                        if (model.getServiceList() != null || model.getServiceList().size() > 0) {
                            for (AppServiceVO serviceEntity : model.getServiceList()) {
                                if (serviceEntity.getId().equals(appServiceEntity.getId())) {
                                    tempServiceEntity = serviceEntity;
                                    deleteServiceList.remove(serviceEntity);
                                    break;
                                }
                            }
                            if (tempServiceEntity != null) {
                                com.xiaoke.entity.kube.entity.AppService appServiceEntityOld = appServiceService.getById(tempServiceEntity.getId());
                                if (appServiceEntityOld != null) {
                                    flag = this.checkUpdateService(appServiceEntity, JoinUtil.entity(appServiceEntityOld, AppServiceVO.class));
                                }
                            }
                        }
                    }
                    V1Service servicesResult = v1ServicesService.getServices(appServiceEntity.getSign(), model.getNamespace());
                    if (flag || servicesResult == null) {
                        V1Service service = new V1Service();
                        V1ObjectMeta serviceMetadata = new V1ObjectMeta();


                        Map<String, String> serviceMetadataLabels = new HashMap<>();
                        serviceMetadataLabels.put("app", appServiceEntity.getSign());
                        serviceMetadata.setLabels(serviceMetadataLabels);
                        serviceMetadata.setName(appServiceEntity.getName());
                        service.setMetadata(serviceMetadata);

                        V1ServiceSpec serviceSpec = new V1ServiceSpec();

                        if (servicesResult != null) {
                            V1ObjectMeta metadata = servicesResult.getMetadata();
                            serviceMetadata.setResourceVersion(metadata.getResourceVersion());

                            V1ServiceSpec spec = servicesResult.getSpec();
                            serviceSpec.setClusterIP(spec.getClusterIP());
                        }

                        if ("Headless".equals(appServiceEntity.getType())) {
                            serviceMetadata.setName(appServiceEntity.getSign());
                            serviceSpec.setClusterIP("None");
                        }
                        List<V1ServicePort> serviceSpecPortsList = new ArrayList<>();
                        List<AppPort> portList = appServiceEntity.getPortList();
                        for (AppPort port : portList) {
                            V1ServicePort serviceSpecPorts = new V1ServicePort();
                            serviceSpecPorts.setName(port.getAgreement().toLowerCase() + "-" + port.getContainer());
                            serviceSpecPorts.setPort(port.getContainer());
                            if (port.getAgreement().equals("HTTP") || port.getAgreement().equals("HTTPS")) {
                                serviceSpecPorts.setProtocol("TCP");
                            } else {
                                serviceSpecPorts.setProtocol(port.getAgreement());
                            }
                            serviceSpecPorts.setTargetPort(new IntOrString(port.getContainer()));
                            if ("NodePort".equals(appServiceEntity.getVisitType())) {
                                if (port.getNode() != null) {
                                    serviceSpecPorts.setNodePort(port.getNode());
                                }
                            }
                            serviceSpecPortsList.add(serviceSpecPorts);
                        }
                        serviceSpec.setPorts(serviceSpecPortsList);

                        Map<String, String> serviceSpecSelector = new HashMap<>();
                        serviceSpecSelector.put("app", model.getSign());
                        serviceSpec.setSelector(serviceSpecSelector);

                        serviceSpec.setType(appServiceEntity.getVisitType());
                        service.setSpec(serviceSpec);
                        if (appServiceEntity.getId() == null) {
                            v1ServicesService.createServices(service, model.getNamespace());
                        } else {
                            if (servicesResult == null) {
                                v1ServicesService.createServices(service, model.getNamespace());
                            } else {
                                v1ServicesService.updateServices(service, appServiceEntity.getSign(), model.getNamespace());
                            }
                        }
                    }
                }
            }

            //删除Service
            if (deleteServiceList != null && deleteServiceList.size() > 0) {
                for (com.xiaoke.entity.kube.entity.AppService appService : deleteServiceList) {
                    v1ServicesService.deleteServices(appService.getSign(), model.getNamespace());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 删除应用
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeAppById(Integer id) {
        log.info("删除应用！id:{}", id);
        return R.ok(this.removeById(id), "删除应用成功！");
    }

    /**
     * 停止应用
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Boolean stop(Integer id) throws Exception {
        AppVO app = this.getAppById(id);
        if (app == null) {
            log.error("停止应用失败，ID：{}", id);
            return Boolean.FALSE;
        }
        app.setState("30");
        this.updateState(app);
        log.info("停止应用！id:{}", id);
        return Boolean.TRUE;
    }

    /**
     * 启动应用
     *
     * @param id
     * @param namespace
     * @return
     * @throws Exception
     */
    @Override
    public Boolean start(Integer id, String namespace) throws Exception {
        AppVO appVO = this.getAppById(id);
        if (appVO == null) {
            log.error("启动应用失败，ID：{}", id);
            return Boolean.FALSE;
        }
        appVO.setState("40");
        AppDTO appDTO = Convert.convert(AppDTO.class, appVO);
        this.updateApp(appDTO);
        log.info("启动应用！id:{}", id);
        return Boolean.TRUE;
    }

    /**
     * 根据应用状态统计数量
     * 此方法用于根据应用的状态，统计每种状态下的应用数量，可选地过滤特定农场、命名空间或类型的应用
     *
     * @param app 应用过滤条件对象，包含农场、命名空间和类型信息，用于筛选特定的应用集合
     * @return 返回一个列表，列表中的每个映射对应一个状态及其对应的应用数量和当前数量总和
     */
    @Override
    public List<Map<String, Object>> stateCount(App app) {
        // 初始化查询包装器
        QueryWrapper<App> query = Wrappers.query();
        // 选择需要查询的字段，包括状态、应用数量和当前数量总和
        query.select("state", "COUNT(id) AS count", "SUM(now_count) AS nowCount");
        // 根据提供的命名空间筛选应用
        if (StrUtil.isNotEmpty(app.getNamespace())) {
            query.lambda().eq(App::getNamespace, app.getNamespace());
        }
        // 根据提供的应用类型筛选应用
        if (StrUtil.isNotEmpty(app.getType())) {
            query.lambda().eq(App::getType, app.getType());
        }
        // 按应用状态分组，以便统计每种状态下的应用数量
        query.lambda().groupBy(App::getState);

        // 执行查询并返回结果列表
        List<Map<String, Object>> mapList = this.listMaps(query);
        return mapList;
    }

    /**
     * 获取已使用端口
     *
     * @param namespace
     * @return
     */
    @Override
    public List<Integer> usePort(String namespace) {
        List<Integer> returnList = new ArrayList<>();
        List<DictItem> usePortList = dictItemService.getDictByType("use_port");
        if (usePortList != null) {
            for (DictItem dictItem : usePortList) {
                returnList.add(Integer.valueOf(dictItem.getValue()));
            }
        }
        QueryWrapper<App> queryApp = Wrappers.query();
        List<App> appList = this.list(queryApp);
        if (CollUtil.isNotEmpty(appList)) {
            List<Integer> appIdList = appList.stream().map(App::getId).collect(Collectors.toList());
            QueryWrapper<com.xiaoke.entity.kube.entity.AppService> queryAppService = Wrappers.query();
            queryAppService.lambda().in(com.xiaoke.entity.kube.entity.AppService::getAppId, appIdList);
            List<com.xiaoke.entity.kube.entity.AppService> appServiceList = appServiceService.list(queryAppService);
            if (CollUtil.isNotEmpty(appServiceList)) {
                List<Integer> appServiceIdList = appServiceList.stream().map(com.xiaoke.entity.kube.entity.AppService::getId).collect(Collectors.toList());
                QueryWrapper<AppPort> queryAppPort = Wrappers.query();
                queryAppPort.lambda().in(AppPort::getServiceId, appServiceIdList);
                queryAppPort.lambda().isNotNull(AppPort::getNode);
                List<AppPort> appPortList = appPortService.list(queryAppPort);
                List<Integer> portList = appPortList.stream().map(AppPort::getNode).collect(Collectors.toList());
                returnList.addAll(portList);
            }
        }
        return returnList;
    }


    /**
     * 修改应用状态
     *
     * @param app
     * @return
     */
    @Override
    public Boolean updateState(AppVO app) throws Exception {
        if ("30".equals(app.getState())) {
            deleteKubeApp(app, Boolean.TRUE);
        }
        App updateApp = new App();
        updateApp.setId(app.getId());
        updateApp.setState(app.getState());
        return this.updateById(updateApp);
    }

    /**
     * 删除k8s应用
     *
     * @param app
     */
    public void deleteKubeApp(AppVO app, Boolean update) throws Exception {
        //删除k8s中应用
        if ("Deployment".equals(app.getType())) {
            Boolean deployment = deploymentService.deleteDeployment(app.getSign(), app.getNamespace());
            if (!deployment) {
                throw new Exception("删除应用失败！");
            }
        } else {
            Boolean statefulSet = statefulSetService.deleteStatefulSet(app.getSign(), app.getNamespace());
            if (!statefulSet) {
                throw new Exception("删除应用失败！");
            }
            //删除有状态应用pod
            for (Integer i = 0; i < app.getCount(); i++) {
                podService.deletePod(app.getSign() + "-" + i, app.getNamespace());
            }
        }

        //删除service
        if (!update) {
            List<AppServiceVO> serviceList = app.getServiceList();
            for (AppServiceVO appServiceEntity : serviceList) {
                v1ServicesService.deleteServices(appServiceEntity.getSign(), app.getNamespace());
            }
        }
        //删除弹性伸缩
        appStretchService.removeAppStretchById(app.getId());
    }


    /**
     * 修改当前应用部署数
     *
     * @param app
     * @return
     */
    @Override
    public Boolean updateNowCount(App app) {
        App updateApp = new App();
        updateApp.setId(app.getId());
        updateApp.setNowCount(app.getNowCount());
        return this.updateById(updateApp);
    }

    @Override
    public Boolean deletePod(String podName, String namespace) {
        podService.deletePod(podName, namespace);
        return Boolean.TRUE;
    }

    /**
     * 修改应用镜像
     *
     * @param app
     * @return
     * @throws Exception
     */
    @Override
    public R<Boolean> updateImage(App app) throws Exception {
        App updateApp = new App();
        updateApp.setId(app.getId());
        updateApp.setImage(app.getImage());
        updateApp.setImageVersion(app.getImageVersion());
        updateApp.setState("70");
        boolean updateFlag = updateApp.updateById();
        if (updateFlag) {
            this.createKubeApp(app.getId());
        }
        return R.ok(Boolean.TRUE, "更新镜像成功！");
    }

    /**
     * 验证是否需要更新service
     *
     * @param newAppService
     * @param oldAppService
     * @return
     */
    public Boolean checkUpdateService(AppServiceVO newAppService, AppServiceVO oldAppService) {
        Boolean isCreate = Boolean.FALSE;
        if (!newAppService.getType().equals(oldAppService.getType())) {
            isCreate = Boolean.TRUE;
        }
        if (newAppService.getName() == null) {
            if (oldAppService.getName() != null) {
                isCreate = Boolean.TRUE;
            }
        } else {
            if (!newAppService.getName().equals(oldAppService.getName())) {
                isCreate = Boolean.TRUE;
            }
        }

        if (!newAppService.getSign().equals(oldAppService.getSign())) {
            isCreate = Boolean.TRUE;
        }

        if (newAppService.getVisitType() == null) {
            if (oldAppService.getVisitType() != null) {
                isCreate = Boolean.TRUE;
            }
        } else {
            if (!newAppService.getVisitType().equals(oldAppService.getVisitType())) {
                isCreate = Boolean.TRUE;
            }
        }

        if (newAppService.getPortList() == null) {
            if (oldAppService.getPortList() != null) {
                isCreate = Boolean.TRUE;
            }
        } else {
            if (oldAppService.getPortList() == null) {
                isCreate = Boolean.TRUE;
            }
            if (newAppService.getPortList().size() != oldAppService.getPortList().size()) {
                isCreate = Boolean.TRUE;
            } else {
                Boolean isBreak = Boolean.FALSE;
                for (Integer j = 0; j < newAppService.getPortList().size(); j++) {
                    AppPort newPort = newAppService.getPortList().get(j);

                    List<AppPort> collect = oldAppService.getPortList().stream().filter(a -> a.getId().equals(newPort.getId())).collect(Collectors.toList());
                    if (collect == null || collect.size() <= 0) {
                        isBreak = Boolean.TRUE;
                        break;
                    }
                    AppPort oldPort = collect.get(0);
                    if (!newPort.getAgreement().equals(oldPort.getAgreement())) {
                        isBreak = Boolean.TRUE;
                        break;
                    }
                    if (!newPort.getContainer().equals(oldPort.getContainer())) {
                        isBreak = Boolean.TRUE;
                        break;
                    }

                    if (newPort.getNode() == null) {
                        if (oldPort.getNode() != null) {
                            isBreak = Boolean.TRUE;
                            break;
                        }
                    } else {
                        if (!newPort.getNode().equals(oldPort.getNode())) {
                            isBreak = Boolean.TRUE;
                            break;
                        }
                    }
                }
                if (isBreak) {
                    isCreate = Boolean.TRUE;
                }
            }
        }
        return isCreate;
    }
}