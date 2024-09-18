package com.xiaoke.extend.kube;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class NodeService {
    private final KubeClient kubeClient;


    /**
     * 获取node列表
     *
     * @return
     */
    public List<V1Node> getNodeList() {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1NodeList v1NodeList = coreV1Api.listNode(null, null, null, null, null, null, null, null, null, null);
            return v1NodeList.getItems();
        } catch (Exception e) {
            log.error("=========> 获取Node列表失败");
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取node
     *
     * @return
     */
    public V1Node getNode(String nodeName) {
        try {
            CoreV1Api coreV1Api = kubeClient.initV1Api();
            V1Node v1Node = coreV1Api.readNode(nodeName, null, null, null);
            return v1Node;
        } catch (Exception e) {
            log.error("=========> 获取Node失败，Node：{}", nodeName);
            e.printStackTrace();
            return null;
        }
    }

}
