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
import com.xiaoke.entity.kube.entity.AppCustomEnv;
import com.xiaoke.model.kube.service.AppCustomEnvService;
/**
* 应用环境变量 Controller
*
*  @author xiaoke
*  @date 2024-08-03 15:45:32
*/
@Api(value = "appCustomEnv", tags = "应用环境变量")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app-custom-env")
public class AppCustomEnvController {

    private final AppCustomEnvService appCustomEnvService;


    @ApiOperation("分页查询应用环境变量")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true),
        @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/page")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,filedName="current", notNull = true, msg = "缺少current参数！"),
        @ValidateFiled(index = 0,filedName="size",  notNull = true, msg = "缺少size参数")
    })
    @Permission("kube_appCustomEnv_view")
    public R<IPage> page(Page page, AppCustomEnv appCustomEnv) {
        return appCustomEnvService.pageAppCustomEnv(page, appCustomEnv);
    }

	@ApiOperation("获取应用环境变量列表")
    @GetMapping("/list")
    @Permission("kube_appCustomEnv_view")
    public R list(AppCustomEnv appCustomEnv) {
        return R.ok(appCustomEnvService.listAppCustomEnv(appCustomEnv));
    }


    @ApiOperation("通过ID查询应用环境变量")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_appCustomEnv_view")
    public R getById(@ApiParam(name = "id", value = "应用环境变量ID", required = true) @PathVariable Integer id) {
        return R.ok(appCustomEnvService.getAppCustomEnvById(id));
    }


    @ApiOperation("添加应用环境变量")
    @SysLog("添加应用环境变量")
    @PostMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,notNull = true,filedName = "appId", msg = "[应用ID]验证未通过！"),
        @ValidateFiled(index = 0,maxLen = 255,filedName = "envKey", msg = "[键]验证未通过！"),
        @ValidateFiled(index = 0,filedName = "envValue", msg = "[值]验证未通过！"),
    })
    @Permission("kube_appCustomEnv_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody AppCustomEnv appCustomEnv) {
        return appCustomEnvService.saveAppCustomEnv(appCustomEnv);
    }


    @ApiOperation("修改应用环境变量")
    @SysLog("修改应用环境变量")
    @PutMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, filedName ="id", msg = "缺少id参数！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "appId", msg = "[应用ID]验证未通过！"),
        @ValidateFiled(index = 0,maxLen = 255,filedName = "envKey", msg = "[键]验证未通过！"),
        @ValidateFiled(index = 0,filedName = "envValue", msg = "[值]验证未通过！")
    })
    @Permission("kube_appCustomEnv_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody AppCustomEnv appCustomEnv) {
        return appCustomEnvService.updateAppCustomEnv(appCustomEnv);
    }


    @ApiOperation("删除应用环境变量")
    @SysLog("删除应用环境变量")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("kube_appCustomEnv_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return appCustomEnvService.removeAppCustomEnvById(id);
    }






}