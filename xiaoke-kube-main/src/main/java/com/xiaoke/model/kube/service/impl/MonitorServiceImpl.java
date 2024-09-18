package com.xiaoke.model.kube.service.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import com.xiaoke.common.core.utils.JsonUtils;
import com.xiaoke.entity.kube.entity.Monitor;
import com.xiaoke.model.kube.service.MonitorService;
import com.xiaoke.properties.ColonyProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service
 *
 * @author chendengwen
 * @date 2019-11-09 23:32:19
 */
@Service
@AllArgsConstructor
public class MonitorServiceImpl implements MonitorService {
    private final ColonyProperties colonyProperties;

    @Override
    public List<Map<String, Object>> getCpu(Monitor monitor) {
        try {
            long time = DateUtil.currentSeconds();
            String url = colonyProperties.getMonitorUrl() + "/api/datasources/proxy/1/api/v1/query_range?query=sum%20(rate%20(container_cpu_usage_seconds_total%7Bimage!%3D%22%22%2Cname%3D~%22%5Ek8s_.*%22%2Ckubernetes_io_hostname%3D~%22%5E.*%24%22%2Cnamespace%3D%22" + monitor.getNamespace() + "%22%2Ccontainer_name%3D%22" + monitor.getContainer() + "%22%7D%5B2m%5D))%20by%20(pod_name)&start=" + (time - 3600) + "&end=" + time + "&step=15";
            HttpRequest accept = HttpRequest.get(url).header("Accept", "application/json").header("Content-Type", "application/json").header("Authorization", "Bearer " + colonyProperties.getMonitorAuth());
            String result = accept.execute().body();
            Map<String, Object> resultMap = new HashMap<>();

            resultMap = JsonUtils.jsonToMap(result);
            if ("success".equals(resultMap.get("status"))) {
                Map<String, Object> data = (Map) resultMap.get("data");
                List<Map<String, Object>> resultList = (List) data.get("result");
                return resultList;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<Map<String, Object>> getMemory(Monitor monitor) {
        try {
            long time = DateUtil.currentSeconds();
            String url = colonyProperties.getMonitorUrl() + "/api/datasources/proxy/1/api/v1/query_range?query=sum%20(container_memory_working_set_bytes%7Bimage!%3D%22%22%2Cname%3D~%22%5Ek8s_.*%22%2Ckubernetes_io_hostname%3D~%22%5E.*%24%22%2Cnamespace%3D%22" + monitor.getNamespace() + "%22%2Ccontainer_name%3D%22" + monitor.getContainer() + "%22%7D)%20by%20(pod_name)&start=" + (time - 3600) + "&end=" + time + "&step=15";
            HttpRequest accept = HttpRequest.get(url).header("Accept", "application/json").header("Content-Type", "application/json").header("Authorization", "Bearer " + colonyProperties.getMonitorAuth());
            String result = accept.execute().body();

            Map<String, Object> resultMap = JsonUtils.jsonToMap(result);
            if ("success".equals(resultMap.get("status"))) {
                Map<String, Object> data = (Map) resultMap.get("data");
                List<Map<String, Object>> resultList = (List) data.get("result");
                return resultList;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Object> getNetWorkIo(Monitor monitor) {
        try {
            long time = DateUtil.currentSeconds();
            String url = colonyProperties.getMonitorUrl() + "/api/datasources/proxy/1/api/v1/query_range?query=sum%20(rate%20(container_network_receive_bytes_total%7Bkubernetes_io_hostname%3D~%22%5E.*%24%22%7D%5B2m%5D))&start=" + (time - 300) + "&end=" + time + "&step=15";
            HttpRequest receivedAccept = HttpRequest.get(url).header("Accept", "application/json").header("Content-Type", "application/json").header("Authorization", "Bearer " + colonyProperties.getMonitorAuth());
            String receivedResult = receivedAccept.execute().body();
            Map<String, Object> receivedMap = new HashMap<>();

            receivedMap = JsonUtils.jsonToMap(receivedResult);

            Map<String, Object> returnMap = new HashMap<>();
            Map<String, List<Double>> valueMap = new HashMap<>();


            if ("success".equals(receivedMap.get("status"))) {
                Map<String, Object> data = (Map) receivedMap.get("data");
                List<Map<String, Object>> receivedList = (List) data.get("result");
                final Map<String, Object> receivedMapList = receivedList.get(0);
                List<List<Object>> values = (List) receivedMapList.get("values");
                List<String> timeList = new ArrayList<>();
                List<Double> valueList = new ArrayList<>();
                for (List value : values) {
                    String format = DateUtil.format(new DateTime(Long.valueOf(value.get(0).toString() + "000")), "HH:mm:ss");
                    timeList.add(format);
                    Double received = Double.valueOf(value.get(1).toString()) / 1024 / 1024;
                    valueList.add(Double.parseDouble(String.format("%.2f", received)));
                }
                valueMap.put("received", valueList);
                returnMap.put("timeList", timeList);
            }


            url = colonyProperties.getMonitorUrl() + "/api/datasources/proxy/1/api/v1/query_range?query=-%20sum%20(rate%20(container_network_transmit_bytes_total%7Bkubernetes_io_hostname%3D~%22%5E.*%24%22%7D%5B2m%5D))&start=" + (time - 300) + "&end=" + time + "&step=15";
            HttpRequest sentAccept = HttpRequest.get(url).header("Accept", "application/json").header("Content-Type", "application/json").header("Authorization", "Bearer " + colonyProperties.getMonitorAuth());
            String sentResult = sentAccept.execute().body();
            Map<String, Object> sentMap = JsonUtils.jsonToMap(sentResult);
            if ("success".equals(sentMap.get("status"))) {
                Map<String, Object> data = (Map) sentMap.get("data");
                List<Map<String, Object>> sentList = (List) data.get("result");
                final Map<String, Object> sentMapList = sentList.get(0);
                List<List<Object>> values = (List) sentMapList.get("values");
                List<Double> valueList = new ArrayList<>();
                for (List value : values) {
                    Double sent = Double.valueOf(value.get(1).toString()) / 1024 / 1024;
                    valueList.add(Double.parseDouble(String.format("%.2f", sent)));
                }
                valueMap.put("sent", valueList);
            }
            returnMap.put("value", valueMap);
            return returnMap;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public Map<String, Double> getClusterMemory(Monitor monitor) {
        try {
            Map<String, Double> returnMap = new HashMap<>();
            long time = DateUtil.currentSeconds();
            String url = colonyProperties.getMonitorUrl() + "/api/datasources/proxy/1/api/v1/query_range?query=sum%20(container_memory_working_set_bytes%7Bid%3D%22%2F%22%2Ckubernetes_io_hostname%3D~%22%5E.*%24%22%7D)&start=" + (time - 30) + "&end=" + time + "&step=15";
            HttpRequest acceptUsed = HttpRequest.get(url).header("Accept", "application/json").header("Content-Type", "application/json").header("Authorization", "Bearer " + colonyProperties.getMonitorAuth());
            String resultUsed = acceptUsed.execute().body();
            Map<String, Object> resultUsedMap = new HashMap<>();

            resultUsedMap = JsonUtils.jsonToMap(resultUsed);

            if ("success".equals(resultUsedMap.get("status"))) {
                Map<String, Object> data = (Map) resultUsedMap.get("data");
                List<Map<String, Object>> resultList = (List) data.get("result");
                final Map<String, Object> sentMapList = resultList.get(0);
                List<List<Object>> values = (List) sentMapList.get("values");
                final List<Object> objects = values.get(values.size() - 1);
                returnMap.put("used", Double.valueOf(objects.get(1).toString()) / 1024 / 1024 / 1024);
            }

            url = colonyProperties.getMonitorUrl() + "/api/datasources/proxy/1/api/v1/query_range?query=sum%20(machine_memory_bytes%7Bkubernetes_io_hostname%3D~%22%5E.*%24%22%7D)&start=" + (time - 30) + "&end=" + time + "&step=15";
            HttpRequest acceptTotal = HttpRequest.get(url).header("Accept", "application/json").header("Content-Type", "application/json").header("Authorization", "Bearer " + colonyProperties.getMonitorAuth());
            String resultTotal = acceptTotal.execute().body();
            Map<String, Object> resultTotalMap = JsonUtils.jsonToMap(resultTotal);
            if ("success".equals(resultTotalMap.get("status"))) {
                Map<String, Object> data = (Map) resultTotalMap.get("data");
                List<Map<String, Object>> resultList = (List) data.get("result");
                final Map<String, Object> sentMapList = resultList.get(0);
                List<List<Object>> values = (List) sentMapList.get("values");
                final List<Object> objects = values.get(values.size() - 1);
                returnMap.put("total", Double.valueOf(objects.get(1).toString()) / 1024 / 1024 / 1024);
            }
            return returnMap;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String, Double> getClusterCpu(Monitor monitor) {
        try {
            Map<String, Double> returnMap = new HashMap<>();
            long time = DateUtil.currentSeconds();
            String url = colonyProperties.getMonitorUrl() + "/api/datasources/proxy/1/api/v1/query_range?query=sum%20(rate%20(container_cpu_usage_seconds_total%7Bid%3D%22%2F%22%2Ckubernetes_io_hostname%3D~%22%5E.*%24%22%7D%5B2m%5D))&start=" + (time - 30) + "&end=" + time + "&step=15";
            HttpRequest acceptUsed = HttpRequest.get(url).header("Accept", "application/json").header("Content-Type", "application/json").header("Authorization", "Bearer " + colonyProperties.getMonitorAuth());
            String resultUsed = acceptUsed.execute().body();
            Map<String, Object> resultUsedMap = new HashMap<>();

            resultUsedMap = JsonUtils.jsonToMap(resultUsed);

            if ("success".equals(resultUsedMap.get("status"))) {
                Map<String, Object> data = (Map) resultUsedMap.get("data");
                List<Map<String, Object>> resultList = (List) data.get("result");
                final Map<String, Object> sentMapList = resultList.get(0);
                List<List<Object>> values = (List) sentMapList.get("values");
                final List<Object> objects = values.get(values.size() - 1);
                returnMap.put("used", Double.valueOf(objects.get(1).toString()));
            }

            url = colonyProperties.getMonitorUrl() + "/api/datasources/proxy/1/api/v1/query_range?query=sum%20(machine_cpu_cores%7Bkubernetes_io_hostname%3D~%22%5E.*%24%22%7D)&start=" + (time - 30) + "&end=" + time + "&step=15";
            HttpRequest acceptTotal = HttpRequest.get(url).header("Accept", "application/json").header("Content-Type", "application/json").header("Authorization", "Bearer " + colonyProperties.getMonitorAuth());
            String resultTotal = acceptTotal.execute().body();
            Map<String, Object> resultTotalMap = JsonUtils.jsonToMap(resultTotal);
            if ("success".equals(resultTotalMap.get("status"))) {
                Map<String, Object> data = (Map) resultTotalMap.get("data");
                List<Map<String, Object>> resultList = (List) data.get("result");
                final Map<String, Object> sentMapList = resultList.get(0);
                List<List<Object>> values = (List) sentMapList.get("values");
                final List<Object> objects = values.get(values.size() - 1);
                returnMap.put("total", Double.valueOf(objects.get(1).toString()));
            }
            return returnMap;
        } catch (Exception e) {
            return null;
        }
    }


}