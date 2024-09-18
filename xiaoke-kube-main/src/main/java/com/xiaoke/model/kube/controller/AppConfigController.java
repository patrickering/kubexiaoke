package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppConfigDTO;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.AppConfig;
import com.xiaoke.model.kube.service.AppConfigService;
import com.xiaoke.model.kube.service.AppService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用配置 Controller
 *
 * @author xiaoke
 * @date 2024-08-03 16:31:30
 */
@Api(value = "appConfig", tags = "应用配置")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app-config")
public class AppConfigController {

    private final AppConfigService appConfigService;
    private final AppService appService;


    @ApiOperation("分页查询应用配置")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_appConfig_view")
    public R<IPage> page(Page page, AppConfig appConfig) {
        return appConfigService.pageAppConfig(page, appConfig);
    }


    @ApiOperation("获取应用配置列表")
    @GetMapping("/list")
    @Permission("kube_appConfig_view")
    public R list(AppConfig appConfig) {
        return R.ok(appConfigService.listAppConfig(appConfig));
    }


    @ApiOperation("通过ID查询应用配置")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_appConfig_view")
    public R getById(@ApiParam(name = "id", value = "应用配置ID", required = true) @PathVariable Integer id) {
        return R.ok(appConfigService.getAppConfigById(id));
    }


    @ApiOperation("添加应用配置")
    @SysLog("添加应用配置")
    @PostMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "appId", msg = "[应用ID]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "versionId", msg = "[配置版本ID]验证未通过！"), @ValidateFiled(index = 0, maxLen = 255, filedName = "catalog", msg = "[挂载目录]验证未通过！"), @ValidateFiled(index = 0, maxLen = 255, filedName = "fileName", msg = "[文件名]验证未通过！"),})
    @Permission("kube_appConfig_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody AppConfig appConfig) {
        return appConfigService.saveAppConfig(appConfig);
    }


    @ApiOperation("修改应用配置")
    @SysLog("修改应用配置")
    @PutMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！"), @ValidateFiled(index = 0, notNull = true, filedName = "appId", msg = "[应用ID]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "versionId", msg = "[配置版本ID]验证未通过！"), @ValidateFiled(index = 0, maxLen = 255, filedName = "catalog", msg = "[挂载目录]验证未通过！"), @ValidateFiled(index = 0, maxLen = 255, filedName = "fileName", msg = "[文件名]验证未通过！")})
    @Permission("kube_appConfig_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody AppConfig appConfig) {
        return appConfigService.updateAppConfig(appConfig);
    }


    @ApiOperation("删除应用配置")
    @SysLog("删除应用配置")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_appConfig_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return appConfigService.removeAppConfigById(id);
    }


    /**
     * 新增配置文件版本
     *
     * @param versionId 版本ID
     * @return R
     */
    @ApiOperation(value = "获取配置文件关联", notes = "获取配置文件关联")
    @SysLog("获取配置文件关联")
    @GetMapping("/version/{versionId}")
    @Permission("kube_config_view")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R getByVersion(@PathVariable("versionId") Integer versionId) {
        return appConfigService.getByVersion(versionId);
    }

    /**
     * 修改应用版本
     *
     * @return
     */
    @ApiOperation(value = "修改应用版本", notes = "修改应用版本")
    @SysLog("修改应用版本")
    @PutMapping("/update-app-version")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R updateAppVersion(@RequestBody AppConfigDTO appConfig) throws Exception {
        appConfigService.updateAppVersion(appConfig);
        List<Integer> idList = appConfig.getIdList();
        for (Integer id : idList) {
            AppConfig config = appConfigService.getById(id);
            App updateApp = new App();
            updateApp.setId(config.getAppId());
            updateApp.setState("70");
            updateApp.updateById();
            appService.createKubeApp(config.getAppId());
        }
        return R.ok(null, "更新成功！");
    }

}