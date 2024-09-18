package com.xiaoke.model.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.Error;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.model.system.service.ErrorService;
import com.xiaoke.utils.SecurityUtils;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * 异常 Controller
 *
 * @author xiaoke
 * @date 2020-08-24 21:37:54
 */
@Api(value = "error", tags = "异常")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.system.prefix}/error")
public class ErrorController {

    private final ErrorService errorService;

    @ApiOperation("分页查询异常")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/page")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"),
            @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")
    })
    @Permission("system_error_view")
    public R<IPage> page(Page page, Error error) {
        QueryWrapper<Error> query = Wrappers.query();
        if (error.getType() != null) {
            query.eq("type", error.getType());
        }
        if (error.getPath() != null) {
            query.like("path", error.getPath());
        }
        query.orderByDesc("id");
        return R.ok(errorService.page(page, query));
    }


    @ApiOperation("获取异常列表")
    @GetMapping("/list")
    @Permission("system_error_view")
    public R list(Error error) {
        QueryWrapper<Error> query = Wrappers.query();
        if (error.getType() != null) {
            query.eq("type", error.getType());
        }
        if (error.getPath() != null) {
            query.like("path", error.getPath());
        }
        return R.ok(errorService.list(query));
    }


    @ApiOperation("通过ID查询异常")
    @GetMapping("/{id}")
    @Permission("system_error_view")
    public R getById(@ApiParam(name = "id", value = "异常ID", required = true) @PathVariable Integer id) {
        return R.ok(errorService.getById(id));
    }


    @ApiOperation("添加异常")
    @SysLog("添加异常")
    @PostMapping
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, filedName = "type", msg = "[类型]验证未通过！"),
            @ValidateFiled(index = 0, notNull = true, maxLen = 255, minLen = 1, filedName = "path", msg = "[路径]验证未通过！"),
            @ValidateFiled(index = 0, notNull = true, filedName = "message", msg = "[异常信息]验证未通过！")
    })
    @Permission("system_error_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody Error error) {
        SysUser user = SecurityUtils.getUser();
        error.setCreateBy(user.getId());
        return R.ok(errorService.save(error), "添加异常成功！");
    }


    @ApiOperation("修改异常")
    @SysLog("修改异常")
    @PutMapping
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！"),
            @ValidateFiled(index = 0, notNull = true, filedName = "type", msg = "[类型]验证未通过！"),
            @ValidateFiled(index = 0, notNull = true, maxLen = 255, minLen = 1, filedName = "path", msg = "[路径]验证未通过！"),
            @ValidateFiled(index = 0, notNull = true, filedName = "message", msg = "[异常信息]验证未通过！")
    })
    @Permission("system_error_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody Error error) {
        SysUser user = SecurityUtils.getUser();
        error.setUpdateBy(user.getId());
        log.info("修改异常！id:{}", error.getId());
        return R.ok(errorService.updateById(error), "修改异常成功！");
    }


    @ApiOperation("删除异常")
    @SysLog("删除异常")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("system_error_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        log.info("删除异常！id:{}", id);
        return R.ok(errorService.removeById(id), "删除异常成功！");
    }
}