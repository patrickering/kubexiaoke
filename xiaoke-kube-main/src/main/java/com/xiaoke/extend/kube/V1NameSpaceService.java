package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class V1NameSpaceService {
    private final KubeClient kubeClient;

    /**
     * 获取命名空间
     *
     * @return
     */
    public V1Namespace getNameSpace(String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            return coreV1Api.readNamespace(namespace, null, null, null);
        } catch (Exception e) {
            log.error("=========> 获取命名空间失败，namespace：{}", namespace);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 创建命名空间
     *
     * @return
     */
    public V1Namespace createNameSpace(V1Namespace namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            return coreV1Api.createNamespace(namespace, null, null, null);
        } catch (Exception e) {
            log.error("=========> 创建命名空间失败");
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 删除命名空间
     *
     * @param namespace
     * @return
     */
    public Boolean deleteNameSpace(String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1Status v1Status = coreV1Api.deleteNamespace(namespace, null, null, null, null, null, null);
            if (v1Status != null) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("=========> 删除命名空间失败");
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }
}
