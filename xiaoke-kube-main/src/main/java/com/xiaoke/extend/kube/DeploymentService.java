package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class DeploymentService {
    private final KubeClient kubeClient;


    /**
     * 获取Deployment
     *
     * @param name
     * @return
     */
    public V1Deployment getDeployment(String name, String namespace) {
        try {
            AppsV1Api appsV1Api = kubeClient.initAppsV1Api();
            V1Deployment namespacedDeployment = appsV1Api.readNamespacedDeployment(name, namespace, null, null, null);
            return namespacedDeployment;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 创建Deployment
     *
     * @param deployment
     * @return
     */
    public V1Deployment createDeployment(V1Deployment deployment, String namespace) {
        try {
            AppsV1Api appsV1Api = kubeClient.initAppsV1Api();
            V1Deployment namespacedDeployment = appsV1Api.createNamespacedDeployment(namespace, deployment, null, null, null);
            return namespacedDeployment;
        } catch (Exception e) {
            log.error("=========> 创建无状态服务失败，namespace：{}", namespace);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新Deployment
     *
     * @param deployment
     * @return
     */
    public V1Deployment updateDeployment(V1Deployment deployment, String name, String namespace) {
        try {
            AppsV1Api appsV1Api = kubeClient.initAppsV1Api();
            V1Deployment namespacedDeployment = appsV1Api.replaceNamespacedDeployment(name, namespace, deployment, null, null, null);
            return namespacedDeployment;
        } catch (Exception e) {
            log.error("=========> 更新无状态服务失败，namespace：{}", namespace);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除Deployment
     *
     * @param name
     * @return
     */
    public Boolean deleteDeployment(String name, String namespace) {
        try {
            AppsV1Api appsV1Api = kubeClient.initAppsV1Api();
            V1Status v1Status = appsV1Api.deleteNamespacedDeployment(name, namespace, null, null, null, null, null, null);
            return v1Status != null;
        } catch (Exception e) {
            log.error("=========> 删除无状态服务失败，namespace：{}", namespace);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }


}
