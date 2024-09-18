package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Secret;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SecretService {
    private final KubeClient kubeClient;

    /**
     * 创建证书
     *
     * @return
     */
    public Boolean createSecret(V1Secret secret, String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1Secret namespacedSecret = coreV1Api.createNamespacedSecret(namespace, secret, null, null, null);
            return namespacedSecret != null;
        } catch (Exception e) {
            log.error("=========> 创建证书失败，namespace：{}", namespace);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

}
