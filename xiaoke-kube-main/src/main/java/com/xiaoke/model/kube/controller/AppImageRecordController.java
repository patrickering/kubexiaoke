package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.AppImageRecord;
import com.xiaoke.model.kube.service.AppImageRecordService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 应用镜像更新记录 Controller
 *
 * @author xiaoke
 * @date 2024-08-14 00:03:31
 */
@Api(value = "appImageRecord", tags = "应用镜像更新记录")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app-image-record")
public class AppImageRecordController {

    private final AppImageRecordService appImageRecordService;


    @ApiOperation("分页查询应用镜像更新记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_app_view")
    public R<IPage> page(Page page, AppImageRecord appImageRecord) {
        return appImageRecordService.pageAppImageRecord(page, appImageRecord);
    }


    @ApiOperation("获取应用镜像更新记录列表")
    @GetMapping("/list")
    @Permission("kube_app_view")
    public R list(AppImageRecord appImageRecord) {
        return R.ok(appImageRecordService.listAppImageRecord(appImageRecord));
    }


    @ApiOperation("通过ID查询应用镜像更新记录")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_app_view")
    public R getById(@ApiParam(name = "id", value = "应用镜像更新记录ID", required = true) @PathVariable Integer id) {
        return R.ok(appImageRecordService.getAppImageRecordById(id));
    }


    @ApiOperation("应用镜像回滚")
    @PutMapping("/go-back/{id}")
    @Permission("kube_app_edit")
    public R goBack(@ApiParam(name = "id", value = "ID", required = true) @PathVariable Integer id) throws Exception {
        return appImageRecordService.goBack(id);
    }
}