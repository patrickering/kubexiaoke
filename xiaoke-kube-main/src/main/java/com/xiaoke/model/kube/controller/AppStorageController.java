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
import com.xiaoke.entity.kube.entity.AppStorage;
import com.xiaoke.model.kube.service.AppStorageService;
/**
* 应用存储 Controller
*
*  @author xiaoke
*  @date 2024-08-03 17:06:23
*/
@Api(value = "appStorage", tags = "应用存储")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app-storage")
public class AppStorageController {

    private final AppStorageService appStorageService;


    @ApiOperation("分页查询应用存储")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true),
        @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/page")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,filedName="current", notNull = true, msg = "缺少current参数！"),
        @ValidateFiled(index = 0,filedName="size",  notNull = true, msg = "缺少size参数")
    })
    @Permission("kube_appStorage_view")
    public R<IPage> page(Page page, AppStorage appStorage) {
        return appStorageService.pageAppStorage(page, appStorage);
    }



	@ApiOperation("获取应用存储列表")
    @GetMapping("/list")
    @Permission("kube_appStorage_view")
    public R list(AppStorage appStorage) {
        return R.ok(appStorageService.listAppStorage(appStorage));
    }


    @ApiOperation("通过ID查询应用存储")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_appStorage_view")
    public R getById(@ApiParam(name = "id", value = "应用存储ID", required = true) @PathVariable Integer id) {
        return R.ok(appStorageService.getAppStorageById(id));
    }


    @ApiOperation("添加应用存储")
    @SysLog("添加应用存储")
    @PostMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,notNull = true,filedName = "appId", msg = "[应用ID]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "storageId", msg = "[存储ID]验证未通过！"),
        @ValidateFiled(index = 0,maxLen = 100,filedName = "name", msg = "[名称]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 255,filedName = "path", msg = "[挂载路径]验证未通过！"),
    })
    @Permission("kube_appStorage_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody AppStorage appStorage) {
        return appStorageService.saveAppStorage(appStorage);
    }


    @ApiOperation("修改应用存储")
    @SysLog("修改应用存储")
    @PutMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, filedName ="id", msg = "缺少id参数！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "appId", msg = "[应用ID]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "storageId", msg = "[存储ID]验证未通过！"),
        @ValidateFiled(index = 0,maxLen = 100,filedName = "name", msg = "[名称]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 255,filedName = "path", msg = "[挂载路径]验证未通过！")
    })
    @Permission("kube_appStorage_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody AppStorage appStorage) {
        return appStorageService.updateAppStorage(appStorage);
    }


    @ApiOperation("删除应用存储")
    @SysLog("删除应用存储")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("kube_appStorage_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return appStorageService.removeAppStorageById(id);
    }






}