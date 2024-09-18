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
import com.xiaoke.entity.kube.entity.AppPort;
import com.xiaoke.model.kube.service.AppPortService;
/**
* 应用端口 Controller
*
*  @author xiaoke
*  @date 2024-08-03 16:44:18
*/
@Api(value = "appPort", tags = "应用端口")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app-port")
public class AppPortController {

    private final AppPortService appPortService;


    @ApiOperation("分页查询应用端口")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true),
        @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/page")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,filedName="current", notNull = true, msg = "缺少current参数！"),
        @ValidateFiled(index = 0,filedName="size",  notNull = true, msg = "缺少size参数")
    })
    @Permission("kube_appPort_view")
    public R<IPage> page(Page page, AppPort appPort) {
        return appPortService.pageAppPort(page, appPort);
    }



	@ApiOperation("获取应用端口列表")
    @GetMapping("/list")
    @Permission("kube_appPort_view")
    public R list(AppPort appPort) {
        return R.ok(appPortService.listAppPort(appPort));
    }


    @ApiOperation("通过ID查询应用端口")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_appPort_view")
    public R getById(@ApiParam(name = "id", value = "应用端口ID", required = true) @PathVariable Integer id) {
        return R.ok(appPortService.getAppPortById(id));
    }


    @ApiOperation("添加应用端口")
    @SysLog("添加应用端口")
    @PostMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,notNull = true,filedName = "serviceId", msg = "[Service ID]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 20,filedName = "agreement", msg = "[访问协议]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "container", msg = "[容器端口]验证未通过！"),
    })
    @Permission("kube_appPort_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody AppPort appPort) {
        return appPortService.saveAppPort(appPort);
    }


    @ApiOperation("修改应用端口")
    @SysLog("修改应用端口")
    @PutMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, filedName ="id", msg = "缺少id参数！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "serviceId", msg = "[Service ID]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 20,filedName = "agreement", msg = "[访问协议]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "container", msg = "[容器端口]验证未通过！")
    })
    @Permission("kube_appPort_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody AppPort appPort) {
        return appPortService.updateAppPort(appPort);
    }


    @ApiOperation("删除应用端口")
    @SysLog("删除应用端口")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("kube_appPort_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return appPortService.removeAppPortById(id);
    }






}