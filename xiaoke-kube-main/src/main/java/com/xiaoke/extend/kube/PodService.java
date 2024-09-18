package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PodService {
    private final KubeClient kubeClient;

    /**
     * 获取pod列表
     *
     * @return
     */
    public V1PodList getPods() {
        log.info("==========> 获取pod");
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            if (coreV1Api == null) {
                return null;
            }
            V1PodList v1PodList = coreV1Api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, 100000, false);
            return v1PodList;
        } catch (Exception e) {
            log.error("获取pod列表异常");
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取pod
     *
     * @param namespace
     * @return
     */
    public V1PodList getNameSpacePods(String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            if (coreV1Api == null) {
                return null;
            }
            V1PodList v1PodList = coreV1Api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, 100000, false);

            return v1PodList;
        } catch (Exception e) {
            log.error("获取pod列表异常，namespace：{}", namespace);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除pod
     *
     * @param namespace
     * @return
     */
    public V1Pod deletePod(String name, String namespace) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1Pod v1Pod = coreV1Api.deleteNamespacedPod(name, namespace, null, null, null, null, null, null);
            return v1Pod;
        } catch (Exception e) {
            log.error("=========> 删除pod失败，namespace：{}，pod：{}", namespace, name);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取pod日志
     *
     * @param namespace
     * @return
     */
    public String podLog(String name, String container, String namespace, Integer limit) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            String log = coreV1Api.readNamespacedPodLog(name, namespace, container, false, null, null, null, null, null, limit, true);
            return log;
        } catch (Exception e) {
            log.error("=========> 删除pod失败，namespace：{}，pod：{}", namespace, name);
            e.printStackTrace();
            return null;
        }
    }
}
