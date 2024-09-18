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
import com.xiaoke.entity.kube.entity.AppService;
import com.xiaoke.model.kube.service.AppServiceService;
/**
* 应用Service Controller
*
*  @author xiaoke
*  @date 2024-08-03 16:39:45
*/
@Api(value = "appService", tags = "应用Service")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app-service")
public class AppServiceController {

    private final AppServiceService appServiceService;


    @ApiOperation("分页查询应用Service")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true),
        @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/page")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,filedName="current", notNull = true, msg = "缺少current参数！"),
        @ValidateFiled(index = 0,filedName="size",  notNull = true, msg = "缺少size参数")
    })
    @Permission("kube_appService_view")
    public R<IPage> page(Page page, AppService appService) {
        return appServiceService.pageAppService(page, appService);
    }



	@ApiOperation("获取应用Service列表")
    @GetMapping("/list")
    @Permission("kube_appService_view")
    public R list(AppService appService) {
        return R.ok(appServiceService.listAppService(appService));
    }


    @ApiOperation("通过ID查询应用Service")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_appService_view")
    public R getById(@ApiParam(name = "id", value = "应用ServiceID", required = true) @PathVariable Integer id) {
        return R.ok(appServiceService.getAppServiceById(id));
    }


    @ApiOperation("添加应用Service")
    @SysLog("添加应用Service")
    @PostMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,notNull = true,filedName = "appId", msg = "[应用ID]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 10,filedName = "type", msg = "[类型]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 100,filedName = "name", msg = "[名称]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 100,filedName = "sign", msg = "[标识]验证未通过！"),
        @ValidateFiled(index = 0,maxLen = 10,filedName = "visitType", msg = "[访问类型]验证未通过！"),
    })
    @Permission("kube_appService_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody AppService appService) {
        return appServiceService.saveAppService(appService);
    }


    @ApiOperation("修改应用Service")
    @SysLog("修改应用Service")
    @PutMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, filedName ="id", msg = "缺少id参数！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "appId", msg = "[应用ID]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 10,filedName = "type", msg = "[类型]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 100,filedName = "name", msg = "[名称]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 100,filedName = "sign", msg = "[标识]验证未通过！"),
        @ValidateFiled(index = 0,maxLen = 10,filedName = "visitType", msg = "[访问类型]验证未通过！")
    })
    @Permission("kube_appService_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody AppService appService) {
        return appServiceService.updateAppService(appService);
    }


    @ApiOperation("删除应用Service")
    @SysLog("删除应用Service")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("kube_appService_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return appServiceService.removeAppServiceById(id);
    }






}