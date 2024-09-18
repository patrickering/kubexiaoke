package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppDomainDTO;
import com.xiaoke.entity.kube.entity.AppDomain;
import com.xiaoke.model.kube.service.AppDomainService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 应用域名 Controller
 *
 * @author xiaoke
 * @date 2024-08-19 00:39:43
 */
@Api(value = "appDomain", tags = "应用域名")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app-domain")
public class AppDomainController {

    private final AppDomainService appDomainService;


    @ApiOperation("分页查询应用域名")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_appDomain_view")
    public R<IPage> page(Page page, AppDomain appDomain, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        appDomain.setNamespace(namespace);
        return appDomainService.pageAppDomain(page, appDomain);
    }


    @ApiOperation("获取应用域名列表")
    @GetMapping("/list")
    @Permission("kube_appDomain_view")
    public R list(AppDomain appDomain, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        appDomain.setNamespace(namespace);
        return R.ok(appDomainService.listAppDomain(appDomain));
    }


    @ApiOperation("通过ID查询应用域名")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_appDomain_view")
    public R getById(@ApiParam(name = "id", value = "应用域名ID", required = true) @PathVariable Integer id) {
        return R.ok(appDomainService.getAppDomainById(id));
    }


    @ApiOperation("添加应用域名")
    @SysLog("添加应用域名")
    @PostMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, maxLen = 20, filedName = "protocol", msg = "[协议]验证未通过！"), @ValidateFiled(index = 0, notNull = true, maxLen = 50, filedName = "domainPrefix", msg = "[域名前缀]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "domainId", msg = "[域名ID]验证未通过！"), @ValidateFiled(index = 0, filedName = "annotations", msg = "[注解]验证未通过！"),})
    @Permission("kube_appDomain_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody AppDomainDTO appDomain, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) throws Exception {
        appDomain.setNamespace(namespace);
        return appDomainService.saveAppDomain(appDomain);
    }


    @ApiOperation("修改应用域名")
    @SysLog("修改应用域名")
    @PutMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！"), @ValidateFiled(index = 0, notNull = true, maxLen = 100, filedName = "namespace", msg = "[命名空间]验证未通过！"), @ValidateFiled(index = 0, notNull = true, maxLen = 20, filedName = "protocol", msg = "[协议]验证未通过！"), @ValidateFiled(index = 0, notNull = true, maxLen = 50, filedName = "domainPrefix", msg = "[域名前缀]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "domainId", msg = "[域名ID]验证未通过！"), @ValidateFiled(index = 0, filedName = "annotations", msg = "[注解]验证未通过！")})
    @Permission("kube_appDomain_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody AppDomain appDomain) {
        return appDomainService.updateAppDomain(appDomain);
    }


    @ApiOperation("删除应用域名")
    @SysLog("删除应用域名")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_appDomain_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return appDomainService.removeAppDomainById(id);
    }


}