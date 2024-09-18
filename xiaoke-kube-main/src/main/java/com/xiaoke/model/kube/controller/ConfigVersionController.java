package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.ConfigVersion;
import com.xiaoke.model.kube.service.ConfigVersionService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置版本 Controller
 *
 * @author xiaoke
 * @date 2024-08-03 16:25:15
 */
@Api(value = "configVersion", tags = "配置版本")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/config-version")
public class ConfigVersionController {

    private final ConfigVersionService configVersionService;


    @ApiOperation("分页查询配置版本")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_configVersion_view")
    public R<IPage> page(Page page, ConfigVersion configVersion) {
        return configVersionService.pageConfigVersion(page, configVersion);
    }


    @ApiOperation("获取配置版本列表")
    @GetMapping("/list")
    @Permission("kube_configVersion_view")
    public R list(ConfigVersion configVersion) {
        return R.ok(configVersionService.listConfigVersion(configVersion));
    }


    @ApiOperation("通过ID查询配置版本")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_configVersion_view")
    public R getById(@ApiParam(name = "id", value = "配置版本ID", required = true) @PathVariable Integer id) {
        return R.ok(configVersionService.getConfigVersionById(id));
    }


    @ApiOperation("添加配置版本")
    @SysLog("添加配置版本")
    @PostMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "configId", msg = "[配置ID]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "version", msg = "[版本]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "content", msg = "[内容]验证未通过！"),})
    @Permission("kube_config_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody ConfigVersion configVersion, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) throws Exception {
        return configVersionService.saveConfigVersion(configVersion, namespace);
    }


    @ApiOperation("修改配置版本")
    @SysLog("修改配置版本")
    @PutMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！"), @ValidateFiled(index = 0, notNull = true, filedName = "configId", msg = "[配置ID]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "version", msg = "[版本]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "content", msg = "[内容]验证未通过！")})
    @Permission("kube_configVersion_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody ConfigVersion configVersion) {
        return configVersionService.updateConfigVersion(configVersion);
    }


    @ApiOperation("删除配置版本")
    @SysLog("删除配置版本")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_configVersion_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return configVersionService.removeConfigVersionById(id);
    }


    /**
     * 根据配置文件查询
     *
     * @param configId 配置文件ID
     * @return R
     */
    @ApiOperation(value = "根据配置文件查询", notes = "根据配置文件查询")
    @GetMapping("config/{configId}")
    @Permission("kube_config_view")
    public R getByConfig(@PathVariable("configId") Integer configId) {
        ConfigVersion configVersion = new ConfigVersion();
        configVersion.setConfigId(configId);
        QueryWrapper<ConfigVersion> query = Wrappers.query(configVersion);
        query.orderByDesc("id");
        List<ConfigVersion> list = configVersionService.list(query);
        return R.ok(list);
    }


}