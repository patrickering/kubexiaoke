package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.ProjectUser;
import com.xiaoke.model.kube.service.ProjectUserService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 项目人员 Controller
 *
 * @author xiaoke
 * @date 2024-08-27 00:01:32
 */
@Api(value = "projectUser", tags = "项目人员")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/project-user")
public class ProjectUserController {

    private final ProjectUserService projectUserService;


    @ApiOperation("分页查询项目人员")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_projectUser_view")
    public R<IPage> page(Page page, ProjectUser projectUser) {
        return projectUserService.pageProjectUser(page, projectUser);
    }


    @ApiOperation("获取项目人员列表")
    @GetMapping("/list")
    @Permission("kube_projectUser_view")
    public R list(ProjectUser projectUser) {
        return R.ok(projectUserService.listProjectUser(projectUser));
    }


    @ApiOperation("通过ID查询项目人员")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_projectUser_view")
    public R getById(@ApiParam(name = "id", value = "项目人员ID", required = true) @PathVariable Integer id) {
        return R.ok(projectUserService.getProjectUserById(id));
    }


    @ApiOperation("添加项目人员")
    @SysLog("添加项目人员")
    @PostMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "projectId", msg = "[项目ID]验证未通过！"), @ValidateFiled(index = 0, notNull = true, filedName = "userId", msg = "[用户ID]验证未通过！"),})
    @Permission("kube_projectUser_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody ProjectUser projectUser) {
        return projectUserService.saveProjectUser(projectUser);
    }


    @ApiOperation("修改项目人员")
    @SysLog("修改项目人员")
    @PutMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！")})
    @Permission("kube_projectUser_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody ProjectUser projectUser) {
        return projectUserService.updateProjectUser(projectUser);
    }


    @ApiOperation("删除项目人员")
    @SysLog("删除项目人员")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_projectUser_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return projectUserService.removeProjectUserById(id);
    }


}