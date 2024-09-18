package com.xiaoke.model.kube.service;


import com.xiaoke.entity.kube.entity.Monitor;

import java.util.List;
import java.util.Map;

/**
 * Service
 *
 * @author chendengwen
 * @date 2019-11-09 23:32:19
 */
public interface MonitorService {
    /**
     * 容器cpu
     *
     * @param monitor
     * @return
     */
    List<Map<String, Object>> getCpu(Monitor monitor);

    /**
     * 容器内存
     *
     * @param monitor
     * @return
     */
    List<Map<String, Object>> getMemory(Monitor monitor);

    /**
     * 集群IO
     *
     * @param monitor
     * @return
     */
    Map<String, Object> getNetWorkIo(Monitor monitor);

    /**
     * 集群内存情况
     *
     * @param monitor
     * @return
     */
    Map<String, Double> getClusterMemory(Monitor monitor);

    /**
     * 集群cpu情况
     *
     * @param monitor
     * @return
     */
    Map<String, Double> getClusterCpu(Monitor monitor);
}