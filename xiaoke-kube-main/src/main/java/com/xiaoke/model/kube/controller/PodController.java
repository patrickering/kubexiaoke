package com.xiaoke.model.kube.controller;

import cn.hutool.core.collection.CollUtil;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Pod;
import com.xiaoke.extend.kube.PodService;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * pod Controller
 *
 * @author xiaoke
 * @date 2024-08-03 15:34:01
 */
@Api(value = "node", tags = "pod")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/pod")
public class PodController {
    private final PodService podService;

    @ApiOperation("获取Pod")
    @GetMapping("pod-list")
    public R<List<Pod>> nodeList() {
        V1PodList pods = podService.getPods();
        List<V1Pod> items = pods.getItems();
        List<Pod> podList = new ArrayList<>();
        for (V1Pod item : items) {
            Pod pod = new Pod();
            pod.setName(item.getMetadata().getName());
            pod.setNodeName(item.getSpec().getNodeName());
            //cpu请求
            BigDecimal cpuRequest = BigDecimal.ZERO;
            //cpu上限
            BigDecimal cpuLimit = BigDecimal.ZERO;

            //内存请求
            BigDecimal memoryRequest = BigDecimal.ZERO;
            //内存上限
            BigDecimal memoryLimit = BigDecimal.ZERO;
            for (V1Container container : item.getSpec().getContainers()) {
                if (container.getResources() != null) {
                    try {
                        if (container.getResources().getRequests() != null) {
                            if (container.getResources().getRequests().get("cpu") != null) {
                                cpuRequest = cpuRequest.add(container.getResources().getRequests().get("cpu").getNumber());
                            }
                            if (container.getResources().getRequests().get("memory") != null) {
                                memoryRequest = memoryRequest.add(container.getResources().getRequests().get("memory").getNumber());
                            }
                        }
                        if (container.getResources().getLimits() != null) {
                            if (container.getResources().getLimits().get("cpu") != null) {
                                cpuLimit = cpuLimit.add(container.getResources().getLimits().get("cpu").getNumber());
                            }
                            if (container.getResources().getLimits().get("memory") != null) {
                                memoryLimit = memoryLimit.add(container.getResources().getLimits().get("memory").getNumber());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            pod.setCpuRequest(cpuRequest);
            pod.setCpuLimit(cpuLimit);
            pod.setMemoryRequest(memoryRequest);
            pod.setMemoryLimit(memoryLimit);
            pod.setPhase(item.getStatus().getPhase());
            pod.setRestartCount(0);
            if (CollUtil.isNotEmpty(item.getStatus().getContainerStatuses())) {
                Integer restartCount = item.getStatus().getContainerStatuses().get(0).getRestartCount();
                DateTime createTime = item.getStatus().getContainerStatuses().get(0).getState().getRunning().getStartedAt();
                pod.setRestartCount(restartCount);
                pod.setCreateTime(createTime);
            }
            podList.add(pod);
        }
        return R.ok(podList);
    }
}