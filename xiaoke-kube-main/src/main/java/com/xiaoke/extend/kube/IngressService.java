package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.openapi.models.ExtensionsV1beta1Ingress;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class IngressService {
    private final KubeClient kubeClient;

    /**
     * 创建Ingress
     *
     * @return
     */
    public Boolean createIngress(ExtensionsV1beta1Ingress ingress, String namespace) {
        try {
            ExtensionsV1beta1Api extensionsV1beta1Api = kubeClient.initExtensionsV1beta1Api();
            ExtensionsV1beta1Ingress namespacedIngress = extensionsV1beta1Api.createNamespacedIngress(namespace, ingress, null, null, null);
            return namespacedIngress != null;
        } catch (Exception e) {
            log.error("=========> 创建Ingress失败，namespace：{}", namespace);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }


    /**
     * 更新Ingress
     *
     * @param ingress
     * @return
     */
    public Boolean updateIngress(ExtensionsV1beta1Ingress ingress, String name, String namespace) {
        try {
            ExtensionsV1beta1Api extensionsV1beta1Api = kubeClient.initExtensionsV1beta1Api();
            ExtensionsV1beta1Ingress namespacedIngress = extensionsV1beta1Api.replaceNamespacedIngress(name, namespace, ingress, null, null, null);
            return namespacedIngress != null;
        } catch (Exception e) {
            log.error("=========> 更新Ingress失败，namespace：{}，ingress：{}", namespace, name);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }


    /**
     * 删除Ingress
     *
     * @param name
     * @return
     */
    public Boolean deleteIngress(String name, String namespace) {
        try {
            ExtensionsV1beta1Api extensionsV1beta1Api = kubeClient.initExtensionsV1beta1Api();
            V1Status v1Status = extensionsV1beta1Api.deleteNamespacedIngress(name, namespace, null, null, null, null, null, null);
            return v1Status != null;
        } catch (Exception e) {
            log.error("=========> 更新Ingress失败，namespace：{}，ingress：{}", namespace, name);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
