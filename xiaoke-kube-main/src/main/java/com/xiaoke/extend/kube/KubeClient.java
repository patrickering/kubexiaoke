package com.xiaoke.extend.kube;


import com.xiaoke.properties.ColonyProperties;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.AutoscalingV2beta1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class KubeClient {
    private final ColonyProperties colonyProperties;

    /**
     * 获取api客户端
     *
     * @return
     */
    private ApiClient getApiClient() {
        ApiClient apiClient = KubeCache.kubeClient;
        if (apiClient == null) {
            log.info("初始化集群");
            apiClient = new ClientBuilder().setBasePath(colonyProperties.getUrl()).setVerifyingSsl(false).setAuthentication(new AccessTokenAuthentication(colonyProperties.getAuthorization())).build();
            apiClient.setConnectTimeout(20000);
            KubeCache.kubeClient = apiClient;
        }
        return apiClient;
    }


    public CoreV1Api initV1Api() {
        try {
            CoreV1Api api = new CoreV1Api();
            ApiClient apiClient = this.getApiClient();
            api.setApiClient(apiClient);
            return api;
        } catch (Exception e) {
            log.error("=========> 初始化集群失败！");
            e.printStackTrace();
            return null;
        }
    }


    public ExtensionsV1beta1Api initExtensionsV1beta1Api() {
        try {
            ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();
            ApiClient apiClient = this.getApiClient();
            api.setApiClient(apiClient);
            return api;
        } catch (Exception e) {
            log.error("=========> 初始化集群失败！");
            e.printStackTrace();
            return null;
        }
    }

    public AppsV1Api initAppsV1Api() {
        try {
            AppsV1Api api = new AppsV1Api();
            ApiClient apiClient = this.getApiClient();
            api.setApiClient(apiClient);
            return api;
        } catch (Exception e) {
            log.error("=========> 初始化集群失败！");
            e.printStackTrace();
            return null;
        }
    }

    public AutoscalingV2beta1Api initAutoscalingV2beta1Api() {
        try {
            AutoscalingV2beta1Api api = new AutoscalingV2beta1Api();
            ApiClient apiClient = this.getApiClient();
            api.setApiClient(apiClient);
            return api;
        } catch (Exception e) {
            log.error("=========> 初始化集群失败！");
            e.printStackTrace();
            return null;
        }
    }
}
