package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Namespace;
import com.xiaoke.model.kube.service.NamespaceService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 命名空间 Controller
 *
 * @author xiaoke
 * @date 2024-08-03 15:00:35
 */
@Api(value = "namespace", tags = "命名空间")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/namespace")
public class NamespaceController {

    private final NamespaceService namespaceService;


    @ApiOperation("分页查询命名空间")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_namespace_view")
    public R<IPage> page(Page page, Namespace namespace) {
        return namespaceService.pageNamespace(page, namespace);
    }


    @ApiOperation("获取命名空间列表")
    @GetMapping("/list")
    @Permission("kube_namespace_view")
    public R list(Namespace namespace) {
        return R.ok(namespaceService.listNamespace(namespace));
    }


    @ApiOperation("通过ID查询命名空间")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_namespace_view")
    public R getById(@ApiParam(name = "id", value = "命名空间ID", required = true) @PathVariable Integer id) {
        return R.ok(namespaceService.getNamespaceById(id));
    }

    @ApiOperation("获取当前命名空间")
    @GetMapping("/current")
    @Permission("kube_namespace_view")
    public R<Namespace> current(@RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        return R.ok(namespaceService.currentNamespace(namespace));
    }

    @ApiOperation("添加命名空间")
    @SysLog("添加命名空间")
    @PostMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "projectId", msg = "[项目id]验证未通过！"), @ValidateFiled(index = 0, maxLen = 100, filedName = "name", msg = "[名称]验证未通过！"), @ValidateFiled(index = 0, maxLen = 100, filedName = "sign", msg = "[标识]验证未通过！"),})
    @Permission("kube_namespace_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody Namespace namespace) throws Exception {
        return namespaceService.saveNamespace(namespace);
    }


    @ApiOperation("修改命名空间")
    @SysLog("修改命名空间")
    @PutMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！"), @ValidateFiled(index = 0, notNull = true, filedName = "projectId", msg = "[项目id]验证未通过！"), @ValidateFiled(index = 0, maxLen = 100, filedName = "name", msg = "[名称]验证未通过！"), @ValidateFiled(index = 0, maxLen = 100, filedName = "sign", msg = "[标识]验证未通过！")})
    @Permission("kube_namespace_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody Namespace namespace) {
        return namespaceService.updateNamespace(namespace);
    }


    @ApiOperation("删除命名空间")
    @SysLog("删除命名空间")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_namespace_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return namespaceService.removeNamespaceById(id);
    }


}