package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Storage;
import com.xiaoke.model.kube.service.StorageService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 存储 Controller
 *
 * @author xiaoke
 * @date 2024-08-03 17:00:02
 */
@Api(value = "storage", tags = "存储")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/storage")
public class StorageController {

    private final StorageService storageService;


    @ApiOperation("分页查询存储")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_storage_view")
    public R<IPage> page(Page page, Storage storage, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        storage.setNamespace(namespace);
        return storageService.pageStorage(page, storage);
    }


    @ApiOperation("获取存储列表")
    @GetMapping("/list")
    @Permission("kube_storage_view")
    public R list(Storage storage, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        storage.setNamespace(namespace);
        return R.ok(storageService.listStorage(storage));
    }


    @ApiOperation("通过ID查询存储")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_storage_view")
    public R getById(@ApiParam(name = "id", value = "存储ID", required = true) @PathVariable Integer id) {
        return R.ok(storageService.getStorageById(id));
    }


    @ApiOperation("添加存储")
    @SysLog("添加存储")
    @PostMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, maxLen = 100, filedName = "name", msg = "[名称]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "capacity", msg = "[容量]验证未通过！"), @ValidateFiled(index = 0, notNull = true, maxLen = 20, filedName = "strategy", msg = "[读写策略]验证未通过！"), @ValidateFiled(index = 0, maxLen = 255, filedName = "remark", msg = "[描述]验证未通过！"),})
    @Permission("kube_storage_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody Storage storage, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) throws Exception {
        return storageService.saveStorage(storage, namespace);
    }

    @ApiOperation("删除存储")
    @SysLog("删除存储")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_storage_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return storageService.removeStorageById(id);
    }

}