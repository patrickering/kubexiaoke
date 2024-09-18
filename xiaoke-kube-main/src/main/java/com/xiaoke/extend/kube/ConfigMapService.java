package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ConfigMapService {
    private final KubeClient kubeClient;

    /**
     * 创建配置
     *
     * @return
     */
    public Boolean createConfigMap(V1ConfigMap configMap, String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1ConfigMap namespacedConfigMap = coreV1Api.createNamespacedConfigMap(namespace, configMap, null, null, null);
            return namespacedConfigMap != null;
        } catch (Exception e) {
            log.error("=========> 创建配置失败，namespace：{}", namespace);
            e.printStackTrace();
            return Boolean.FALSE;
        }

    }

}
