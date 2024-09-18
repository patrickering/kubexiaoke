package com.xiaoke.model.kube.controller;

import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.extend.kube.NodeService;
import com.xiaoke.extend.kube.PodService;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1PodList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 节点 Controller
 *
 * @author xiaoke
 * @date 2024-08-03 15:34:01
 */
@Api(value = "node", tags = "节点")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/node")
public class NodeController {
    private final NodeService nodeService;
    private final PodService podService;

    @ApiOperation("获取节点")
    @GetMapping("node-list")
    public R<List<V1Node>> nodeList() {
        List<V1Node> nodeList = nodeService.getNodeList();


        return R.ok(nodeList);
    }

    @ApiOperation("获取节点")
    @GetMapping("node/{nodeName}")
    public R<V1Node> nodeList(@PathVariable String nodeName) {
        V1Node node = nodeService.getNode(nodeName);
        return R.ok(node);
    }
}