package com.xiaoke.model.kube.controller;


import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Monitor;
import com.xiaoke.model.kube.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controller
 * 监控
 *
 * @author chendengwen
 * @date 2019-11-09 23:51:39
 */
@Api(value = "app", tags = "监控")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/monitor")
public class MonitorController {
    private final MonitorService monitorService;

    @ApiOperation("获取应用cpu使用量")
    @GetMapping("/cpu")
    @Permission("kube_app_view")
    public R<List<Map<String, Object>>> cpu(Monitor monitor, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        monitor.setNamespace(namespace);
        List<Map<String, Object>> cpu = monitorService.getCpu(monitor);
        return R.ok(cpu);
    }


    @ApiOperation("获取应用内存使用量")
    @GetMapping("/memory")
    @Permission("kube_app_view")
    public R<List<Map<String, Object>>> memory(Monitor monitor, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        monitor.setNamespace(namespace);
        List<Map<String, Object>> memory = monitorService.getMemory(monitor);
        return R.ok(memory);
    }

    @ApiOperation("获取网络IO")
    @GetMapping("/network/io")
    @Permission("kube_app_view")
    public R<Map<String, Object>> networkIo(Monitor monitor) {
        Map<String, Object> netWorkIo = monitorService.getNetWorkIo(monitor);
        return R.ok(netWorkIo);
    }

    @ApiOperation("获取集群内存")
    @GetMapping("/cluster/memory")
    @Permission("kube_app_view")
    public R<Map<String, Double>> clusterMemory(Monitor monitor) {
        final Map<String, Double> clusterMemory = monitorService.getClusterMemory(monitor);
        return R.ok(clusterMemory);
    }

    @ApiOperation("获取集群CPU")
    @GetMapping("/cluster/cpu")
    @Permission("kube_app_view")
    public R<Map<String, Double>> clusterCpu(Monitor monitor) {
        final Map<String, Double> clusterMemory = monitorService.getClusterCpu(monitor);
        return R.ok(clusterMemory);
    }
}
