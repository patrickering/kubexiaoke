package com.xiaoke.model.kube.controller;

import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.AppStretch;
import com.xiaoke.model.kube.service.AppStretchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 弹性伸缩 Controller
 *
 * @author xiaoke
 * @date 2024-08-09 16:29:23
 */
@Api(value = "appStretch", tags = "弹性伸缩")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app-stretch")
public class AppStretchController {

    private final AppStretchService appStretchService;

    @ApiOperation("添加弹性伸缩")
    @SysLog("添加弹性伸缩")
    @PostMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "appId", msg = "[应用ID]验证未通过！"), @ValidateFiled(index = 0, maxLen = 20, filedName = "cpuValueType", msg = "[cpu数值类型（value=数值，proportion=比例）]验证未通过！"), @ValidateFiled(index = 0, maxLen = 20, filedName = "memoryValueType", msg = "[memory数值类型（value=数值，proportion=比例）]验证未通过！"), @ValidateFiled(index = 0, maxLen = 220, filedName = "memoryCompany", msg = "[memory单位]验证未通过！"),})
    @Permission("kube_appStretch_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody AppStretch appStretch, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) throws Exception {
        return appStretchService.saveAppStretch(appStretch, namespace);
    }

    @ApiOperation("删除弹性伸缩")
    @SysLog("删除弹性伸缩")
    @DeleteMapping("/{appId}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_appStretch_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer appId) throws Exception {
        return appStretchService.removeAppStretchById(appId);
    }


}