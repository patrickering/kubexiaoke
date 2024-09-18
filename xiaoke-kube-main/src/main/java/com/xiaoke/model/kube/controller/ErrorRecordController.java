package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.ErrorRecord;
import com.xiaoke.model.kube.service.ErrorRecordService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 异常记录 Controller
 *
 * @author xiaoke
 * @date 2024-08-22 23:06:02
 */
@Api(value = "errorRecord", tags = "异常记录")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/error-record")
public class ErrorRecordController {

    private final ErrorRecordService errorRecordService;


    @ApiOperation("分页查询异常记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_errorRecord_view")
    public R<IPage> page(Page page, ErrorRecord errorRecord, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        errorRecord.setNamespace(namespace);
        return errorRecordService.pageErrorRecord(page, errorRecord);
    }

    @ApiOperation("获取异常记录列表")
    @GetMapping("/list")
    @Permission("kube_errorRecord_view")
    public R list(ErrorRecord errorRecord, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        errorRecord.setNamespace(namespace);
        return R.ok(errorRecordService.listErrorRecord(errorRecord));
    }


    @ApiOperation("通过ID查询异常记录")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_errorRecord_view")
    public R getById(@ApiParam(name = "id", value = "异常记录ID", required = true) @PathVariable Integer id) {
        return R.ok(errorRecordService.getErrorRecordById(id));
    }

    @ApiOperation("删除异常记录")
    @SysLog("删除异常记录")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_errorRecord_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return errorRecordService.removeErrorRecordById(id);
    }

    @ApiOperation("批量删除异常记录")
    @SysLog("批量删除异常记录")
    @DeleteMapping("/batch-delete")
    @Permission("kube_errorRecord_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> batchDelete(@RequestBody List<Integer> idList) {
        return errorRecordService.batchDelete(idList);
    }

    @ApiOperation("修改已读")
    @SysLog("修改已读")
    @PutMapping("/update-read-flag")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！"), @ValidateFiled(index = 0, notNull = true, filedName = "readFlag", msg = "[已读]验证未通过！")})
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> updateReadFlag(@RequestBody ErrorRecord errorRecord) {
        return errorRecordService.updateReadFlag(errorRecord);
    }


}