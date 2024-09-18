package com.xiaoke.model.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.LoginLog;
import com.xiaoke.model.system.service.LoginLogService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * 登录日志 Controller
 *
 * @author xiaoke
 * @date 2022-01-23 15:30:20
 */
@Api(value = "loginLog", tags = "登录日志")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.system.prefix}/login-log")
public class LoginLogController {

    private final LoginLogService loginLogService;


    @ApiOperation("分页查询登录日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/page")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"),
            @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")
    })
    @Permission("system_loginLog_view")
    public R<IPage> page(Page page, LoginLog loginLog) {
        return loginLogService.pageLoginLog(page, loginLog);
    }


    @ApiOperation("获取登录日志列表")
    @GetMapping("/list")
    @Permission("system_loginLog_view")
    public R list(LoginLog loginLog) {
        return loginLogService.listLoginLog(loginLog);
    }


    @ApiOperation("通过ID查询登录日志")
    @GetMapping("/get-by-id/{id}")
    @Permission("system_loginLog_view")
    public R getById(@ApiParam(name = "id", value = "登录日志ID", required = true) @PathVariable Integer id) {
        return loginLogService.getLoginLogById(id);
    }

    @ApiOperation("删除登录日志")
    @SysLog("删除登录日志")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("system_loginLog_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return loginLogService.removeLoginLogById(id);
    }


}