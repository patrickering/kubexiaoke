package com.xiaoke.model.kube.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.AppStretch;
import com.xiaoke.extend.kube.HorizontalPodAutoScalerService;
import com.xiaoke.model.kube.mapper.AppMapper;
import com.xiaoke.model.kube.mapper.AppStretchMapper;
import com.xiaoke.model.kube.service.AppStretchService;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 弹性伸缩 Service
 *
 * @author xiaoke
 * @date 2024-08-09 16:29:23
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppStretchServiceImpl extends ServiceImpl<AppStretchMapper, AppStretch> implements AppStretchService {
    private final AppStretchMapper appStretchMapper;
    private final AppMapper appMapper;
    private final HorizontalPodAutoScalerService horizontalPodAutoScalerService;


    /**
     * 添加弹性伸缩
     *
     * @param appStretch
     * @return
     */
    @Override
    public R<Boolean> saveAppStretch(AppStretch appStretch, String namespace) throws Exception {
        App app = appMapper.selectById(appStretch.getAppId());
        //先删除
        if (appStretch.getId() != null) {
            this.removeById(appStretch.getId());
        }
        log.info("添加弹性伸缩！弹性伸缩:{}", appStretch);
        Boolean saveFlag = this.save(appStretch);
        if (saveFlag) {
            // 删除k8s弹性伸缩
            horizontalPodAutoScalerService.deleteHorizontalPodAutoScaler(namespace, app.getSign());
            // 添加k8s弹性伸缩
            V2beta1HorizontalPodAutoscaler horizontalPodAutoScaler = new V2beta1HorizontalPodAutoscaler();
            V1ObjectMeta horizontalPodAutoScalerMetadata = new V1ObjectMeta();
            horizontalPodAutoScalerMetadata.setName(app.getSign());
            horizontalPodAutoScalerMetadata.setNamespace(app.getNamespace());
            horizontalPodAutoScaler.setMetadata(horizontalPodAutoScalerMetadata);

            V2beta1HorizontalPodAutoscalerSpec horizontalPodAutoScalerSpec = new V2beta1HorizontalPodAutoscalerSpec();
            V2beta1CrossVersionObjectReference horizontalPodAutoScalerSpecScaleTargetRef = new V2beta1CrossVersionObjectReference();
            horizontalPodAutoScalerSpecScaleTargetRef.setKind("Deployment");
            horizontalPodAutoScalerSpecScaleTargetRef.setName(app.getSign());
            horizontalPodAutoScalerSpec.setScaleTargetRef(horizontalPodAutoScalerSpecScaleTargetRef);
            //副本设置
            horizontalPodAutoScalerSpec.setMinReplicas(appStretch.getMinReplicas());
            horizontalPodAutoScalerSpec.setMaxReplicas(appStretch.getMaxReplicas());
            List<V2beta1MetricSpec> horizontalPodAutoScalerSpecMetricsList = new ArrayList<>();
            //cpu指标
            if (appStretch.getCpu()) {
                V2beta1MetricSpec horizontalPodAutoScalerSpecMetrics = new V2beta1MetricSpec();
                horizontalPodAutoScalerSpecMetrics.setType("Resource");
                V2beta1ResourceMetricSource horizontalPodAutoScalerSpecMetricsResource = new V2beta1ResourceMetricSource();
                horizontalPodAutoScalerSpecMetricsResource.setName("cpu");
                if (appStretch.getCpuValueType().equals("proportion")) {
                    horizontalPodAutoScalerSpecMetricsResource.setTargetAverageUtilization(appStretch.getCpuValue().intValue());
                } else if (appStretch.getCpuValueType().equals("value")) {
                    horizontalPodAutoScalerSpecMetricsResource.setTargetAverageValue(new Quantity(appStretch.getCpuValue() * 1000 + "m"));
                }
                horizontalPodAutoScalerSpecMetrics.setResource(horizontalPodAutoScalerSpecMetricsResource);
                horizontalPodAutoScalerSpecMetricsList.add(horizontalPodAutoScalerSpecMetrics);
            }
            //内存指标
            if (appStretch.getMemory()) {
                V2beta1MetricSpec horizontalPodAutoScalerSpecMetrics = new V2beta1MetricSpec();
                horizontalPodAutoScalerSpecMetrics.setType("Resource");
                V2beta1ResourceMetricSource horizontalPodAutoScalerSpecMetricsResource = new V2beta1ResourceMetricSource();
                horizontalPodAutoScalerSpecMetricsResource.setName("memory");
                if (appStretch.getMemoryValueType().equals("proportion")) {
                    horizontalPodAutoScalerSpecMetricsResource.setTargetAverageUtilization(appStretch.getMemoryValue().intValue());
                } else if (appStretch.getMemoryValueType().equals("value")) {
                    horizontalPodAutoScalerSpecMetricsResource.setTargetAverageValue(new Quantity(appStretch.getMemoryValue() + appStretch.getMemoryCompany()));
                }
                horizontalPodAutoScalerSpecMetrics.setResource(horizontalPodAutoScalerSpecMetricsResource);
                horizontalPodAutoScalerSpecMetricsList.add(horizontalPodAutoScalerSpecMetrics);
            }
            horizontalPodAutoScalerSpec.setMetrics(horizontalPodAutoScalerSpecMetricsList);
            horizontalPodAutoScaler.setSpec(horizontalPodAutoScalerSpec);
            Boolean result = horizontalPodAutoScalerService.createHorizontalPodAutoScaler(horizontalPodAutoScaler, namespace);
            if (!result) {
                throw new Exception("创建弹性伸缩失败！");
            }
            return R.ok(Boolean.TRUE, "添加弹性伸缩成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加弹性伸缩失败！");
        }
    }

    /**
     * 删除弹性伸缩
     *
     * @param appId
     * @return
     */
    @Override
    public R<Boolean> removeAppStretchById(Integer appId) throws Exception {
        QueryWrapper<AppStretch> queryAppStretch = Wrappers.query();
        queryAppStretch.lambda().eq(AppStretch::getAppId, appId);
        AppStretch appStretch = this.getOne(queryAppStretch);
        if (appStretch == null) {
            return R.failed("弹性伸缩不存在！");
        }
        App app = appMapper.selectById(appId);
        this.removeById(appStretch.getId());
        Boolean result = horizontalPodAutoScalerService.deleteHorizontalPodAutoScaler(app.getNamespace(), app.getSign());
        if (!result) {
            throw new Exception("关闭失败!");
        }
        return R.ok(Boolean.TRUE, "关闭成功！");
    }


}