package com.xiaoke.model.kube.controller;

import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.xiaoke.annotation.permission.Permission;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.entity.kube.entity.AppHost;
import com.xiaoke.model.kube.service.AppHostService;
/**
* 应用Host Controller
*
*  @author xiaoke
*  @date 2024-08-03 17:10:09
*/
@Api(value = "appHost", tags = "应用Host")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app-host")
public class AppHostController {

    private final AppHostService appHostService;


    @ApiOperation("分页查询应用Host")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true),
        @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/page")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,filedName="current", notNull = true, msg = "缺少current参数！"),
        @ValidateFiled(index = 0,filedName="size",  notNull = true, msg = "缺少size参数")
    })
    @Permission("kube_appHost_view")
    public R<IPage> page(Page page, AppHost appHost) {
        return appHostService.pageAppHost(page, appHost);
    }



	@ApiOperation("获取应用Host列表")
    @GetMapping("/list")
    @Permission("kube_appHost_view")
    public R list(AppHost appHost) {
        return R.ok(appHostService.listAppHost(appHost));
    }


    @ApiOperation("通过ID查询应用Host")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_appHost_view")
    public R getById(@ApiParam(name = "id", value = "应用HostID", required = true) @PathVariable Integer id) {
        return R.ok(appHostService.getAppHostById(id));
    }


    @ApiOperation("添加应用Host")
    @SysLog("添加应用Host")
    @PostMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,notNull = true,filedName = "appId", msg = "[应用ID]验证未通过！"),
        @ValidateFiled(index = 0,maxLen = 100,filedName = "ip", msg = "[Ip]验证未通过！"),
        @ValidateFiled(index = 0,maxLen = 100,filedName = "domain", msg = "[域名]验证未通过！"),
    })
    @Permission("kube_appHost_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody AppHost appHost) {
        return appHostService.saveAppHost(appHost);
    }


    @ApiOperation("修改应用Host")
    @SysLog("修改应用Host")
    @PutMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, filedName ="id", msg = "缺少id参数！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "appId", msg = "[应用ID]验证未通过！"),
        @ValidateFiled(index = 0,maxLen = 100,filedName = "ip", msg = "[Ip]验证未通过！"),
        @ValidateFiled(index = 0,maxLen = 100,filedName = "domain", msg = "[域名]验证未通过！")
    })
    @Permission("kube_appHost_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody AppHost appHost) {
        return appHostService.updateAppHost(appHost);
    }


    @ApiOperation("删除应用Host")
    @SysLog("删除应用Host")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("kube_appHost_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return appHostService.removeAppHostById(id);
    }






}