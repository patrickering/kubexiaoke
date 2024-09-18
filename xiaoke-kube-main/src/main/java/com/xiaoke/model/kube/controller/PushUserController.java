package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.PushUser;
import com.xiaoke.model.kube.service.PushUserService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 推送接收人 Controller
 *
 * @author xiaoke
 * @date 2024-08-28 00:33:59
 */
@Api(value = "pushUser", tags = "推送接收人")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/push-user")
public class PushUserController {

    private final PushUserService pushUserService;


    @ApiOperation("分页查询推送接收人")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_pushUser_view")
    public R<IPage> page(Page page, PushUser pushUser) {
        return pushUserService.pagePushUser(page, pushUser);
    }


    @ApiOperation("获取推送接收人列表")
    @GetMapping("/list")
    @Permission("kube_pushUser_view")
    public R list(PushUser pushUser) {
        return R.ok(pushUserService.listPushUser(pushUser));
    }


    @ApiOperation("通过ID查询推送接收人")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_pushUser_view")
    public R getById(@ApiParam(name = "id", value = "推送接收人ID", required = true) @PathVariable Integer id) {
        return R.ok(pushUserService.getPushUserById(id));
    }


    @ApiOperation("添加推送接收人")
    @SysLog("添加推送接收人")
    @PostMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "projectUserId", msg = "[用户]验证未通过！"), @ValidateFiled(index = 0, notNull = true, maxLen = 255, filedName = "pushType", msg = "[推送类型]验证未通过！"),})
    @Permission("kube_pushUser_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody PushUser pushUser, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        pushUser.setNamespace(namespace);
        return pushUserService.savePushUser(pushUser);
    }


    @ApiOperation("修改推送接收人")
    @SysLog("修改推送接收人")
    @PutMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！"), @ValidateFiled(index = 0, notNull = true, filedName = "projectUserId", msg = "[用户]验证未通过！"), @ValidateFiled(index = 0, notNull = true, maxLen = 255, filedName = "pushType", msg = "[推送类型]验证未通过！")})
    @Permission("kube_pushUser_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody PushUser pushUser, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        pushUser.setNamespace(namespace);
        return pushUserService.updatePushUser(pushUser);
    }


    @ApiOperation("删除推送接收人")
    @SysLog("删除推送接收人")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_pushUser_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return pushUserService.removePushUserById(id);
    }


}