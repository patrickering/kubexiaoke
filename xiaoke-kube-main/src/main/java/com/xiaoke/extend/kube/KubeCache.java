package com.xiaoke.extend.kube;


import io.kubernetes.client.openapi.ApiClient;
import org.springframework.stereotype.Service;

@Service
public class KubeCache {
    public volatile static ApiClient kubeClient = null;
}
