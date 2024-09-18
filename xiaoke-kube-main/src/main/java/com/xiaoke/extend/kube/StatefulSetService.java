package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1StatefulSet;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.Yaml;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 有状态服务
 */
@Service
@Slf4j
@AllArgsConstructor
public class StatefulSetService {
    private final KubeClient kubeClient;


    /**
     * 获取StatefulSet
     *
     * @param name
     * @return
     */
    public V1StatefulSet getStatefulSet(String name, String namespace) {
        try {
            AppsV1Api appsV1Api = kubeClient.initAppsV1Api();
            V1StatefulSet namespacedDeployment = appsV1Api.readNamespacedStatefulSet(name, namespace, null, null, null);
            return namespacedDeployment;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建StatefulSet
     *
     * @param statefulSet
     * @return
     */
    public V1StatefulSet createStatefulSet(V1StatefulSet statefulSet, String namespace) {
        try {
            AppsV1Api appsV1Api = kubeClient.initAppsV1Api();
            V1StatefulSet namespacedStatefulSet = appsV1Api.createNamespacedStatefulSet(namespace, statefulSet, null, null, null);
            return namespacedStatefulSet;
        } catch (Exception e) {
            log.error("=========> 创建有状态服务失败，namespace：{},statefulSet:{}", namespace, statefulSet);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新StatefulSet
     *
     * @param statefulSet
     * @return
     */
    public V1StatefulSet updateStatefulSet(V1StatefulSet statefulSet, String name, String namespace) {
        try {
            AppsV1Api appsV1Api = kubeClient.initAppsV1Api();
            V1StatefulSet namespacedStatefulSet = appsV1Api.replaceNamespacedStatefulSet(name, namespace, statefulSet, null, null, null);
            return namespacedStatefulSet;
        } catch (Exception e) {
            String statefulSetYaml = Yaml.dump(statefulSet);
            log.error("=========> 更新有状态服务失败，namespace：{},statefulSet：{},statefulSet:{}", namespace, name, statefulSetYaml);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除StatefulSet
     *
     * @param name
     * @return
     */
    public Boolean deleteStatefulSet(String name, String namespace) {
        try {
            AppsV1Api appsV1Api = kubeClient.initAppsV1Api();
            V1Status v1Status = appsV1Api.deleteNamespacedStatefulSet(name, namespace, null, null, null, null, null, null);
            return v1Status != null;
        } catch (Exception e) {
            log.error("=========> 删除有状态服务失败，namespace：{},statefulSet：{}", namespace, name);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
