package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class V1ServicesService {
    private final KubeClient kubeClient;

    /**
     * 获取Services
     *
     * @return
     */
    public V1Service getServices(String name, String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1Service v1Service = coreV1Api.readNamespacedService(name, namespace, null, null, null);
            return v1Service;
        } catch (Exception e) {
            log.error("=========> 获取命名空间失败，namespace：{}", namespace);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建Services
     *
     * @return
     */
    public V1Service createServices(V1Service service, String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1Service v1Service = coreV1Api.createNamespacedService(namespace, service, null, null, null);
            return v1Service;
        } catch (Exception e) {
            log.error("=========> 创建Services失败，namespace：{}", namespace);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 修改Services
     *
     * @return
     */
    public V1Service updateServices(V1Service service, String name, String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1Service v1Service = coreV1Api.replaceNamespacedService(name, namespace, service, null, null, null);
            return v1Service;
        } catch (Exception e) {
            log.error("=========> 更新Services失败，namespace：{}，service：{}", namespace, name);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除Services
     *
     * @return
     */
    public Boolean deleteServices(String name, String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1Status v1Status = coreV1Api.deleteNamespacedService(name, namespace, null, null, null, null, null, null);
            return v1Status != null;
        } catch (Exception e) {
            log.error("=========> 删除Services失败，namespace：{}，service：{}", namespace, name);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
