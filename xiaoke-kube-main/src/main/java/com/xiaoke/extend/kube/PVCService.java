package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaim;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PVCService {
    private final KubeClient kubeClient;


    /**
     * 创建存储
     *
     * @return
     */
    public Boolean createPVC(V1PersistentVolumeClaim persistentVolumeClaim, String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1PersistentVolumeClaim namespacedPersistentVolumeClaim = coreV1Api.createNamespacedPersistentVolumeClaim(namespace, persistentVolumeClaim, null, null, null);
            return namespacedPersistentVolumeClaim != null;
        } catch (Exception e) {
            log.error("=========> 创建存储失败，namespace：{}", namespace);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }


    /**
     * 删除存储
     *
     * @return
     */
    public Boolean deletePVC(String name, String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1PersistentVolumeClaim v1PersistentVolumeClaim = coreV1Api.deleteNamespacedPersistentVolumeClaim(name, namespace, null, null, null, null, null, null);
            return v1PersistentVolumeClaim != null;
        } catch (Exception e) {
            log.error("=========> 删除存储失败，namespace：{}，PVC：{}", namespace, name);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

}
