package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AutoscalingV2beta1Api;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.openapi.models.V2beta1HorizontalPodAutoscaler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 弹性伸缩
 */
@Service
@Slf4j
@AllArgsConstructor
public class HorizontalPodAutoScalerService {
    private final KubeClient kubeClient;

    /**
     * 获取弹性伸缩
     *
     * @return
     */
    public V2beta1HorizontalPodAutoscaler getHorizontalPodAutoScaler(String name, String namespace) {
        try {
            AutoscalingV2beta1Api autoscalingV2beta1Api = kubeClient.initAutoscalingV2beta1Api();
            V2beta1HorizontalPodAutoscaler v2beta1HorizontalPodAutoscaler = autoscalingV2beta1Api.readNamespacedHorizontalPodAutoscaler(name, namespace, null, null, null);
            return v2beta1HorizontalPodAutoscaler;
        } catch (Exception e) {
            log.error("=========> 获取弹性伸缩失败，namespace：{}，HorizontalPodAutoScaler：{}", namespace, name);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建弹性伸缩
     *
     * @return
     */
    public Boolean createHorizontalPodAutoScaler(V2beta1HorizontalPodAutoscaler horizontalPodAutoscaler, String namespace) {
        try {
            AutoscalingV2beta1Api autoscalingV2beta1Api = kubeClient.initAutoscalingV2beta1Api();
            V2beta1HorizontalPodAutoscaler v2beta1HorizontalPodAutoscaler = autoscalingV2beta1Api.createNamespacedHorizontalPodAutoscaler(namespace, horizontalPodAutoscaler, null, null, null);
            return v2beta1HorizontalPodAutoscaler != null;
        } catch (Exception e) {
            log.error("=========> 创建弹性伸缩失败，namespace：{}", namespace);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除弹性伸缩
     *
     * @param namespace
     * @return
     */
    public Boolean deleteHorizontalPodAutoScaler(String namespace, String name) {
        try {
            AutoscalingV2beta1Api autoscalingV2beta1Api = kubeClient.initAutoscalingV2beta1Api();
            V1Status v1Status = autoscalingV2beta1Api.deleteNamespacedHorizontalPodAutoscaler(name, namespace, null, null, null, null, null, null);
            return v1Status != null;
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                return Boolean.TRUE;
            }
            log.error("=========> 创建弹性伸缩失败，namespace：{}", namespace);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
