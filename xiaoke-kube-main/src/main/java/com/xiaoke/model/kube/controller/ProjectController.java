package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.ProjectDTO;
import com.xiaoke.entity.kube.entity.Project;
import com.xiaoke.model.kube.service.ProjectService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 项目 Controller
 *
 * @author xiaoke
 * @date 2024-08-03 14:21:50
 */
@Api(value = "project", tags = "项目")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/project")
public class ProjectController {

    private final ProjectService projectService;


    @ApiOperation("分页查询项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/page")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"),
            @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")
    })
    @Permission("kube_project_view")
    public R<IPage> page(Page page, Project project) {
        return projectService.pageProject(page, project);
    }


    @ApiOperation("获取项目列表")
    @GetMapping("/list")
    @Permission("kube_project_view")
    public R list(Project project) {
        return R.ok(projectService.listProject(project));
    }


    @ApiOperation("通过ID查询项目")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_project_view")
    public R getById(@ApiParam(name = "id", value = "项目ID", required = true) @PathVariable Integer id) {
        return R.ok(projectService.getProjectById(id));
    }


    @ApiOperation("添加项目")
    @SysLog("添加项目")
    @PostMapping
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, maxLen = 100, filedName = "title", msg = "[标题]验证未通过！"),
            @ValidateFiled(index = 0, maxLen = 255, filedName = "remark", msg = "[描述]验证未通过！"),
            @ValidateFiled(index = 0, maxLen = 20, filedName = "color", msg = "[颜色]验证未通过！"),
    })
    @Permission("kube_project_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody Project project) {
        return projectService.saveProject(project);
    }

    @ApiOperation("修改项目")
    @SysLog("修改项目")
    @PutMapping
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！"),
            @ValidateFiled(index = 0, notNull = true, maxLen = 100, filedName = "title", msg = "[标题]验证未通过！"),
            @ValidateFiled(index = 0, maxLen = 255, filedName = "remark", msg = "[描述]验证未通过！"),
            @ValidateFiled(index = 0, maxLen = 20, filedName = "color", msg = "[颜色]验证未通过！")
    })
    @Permission("kube_project_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody Project project) {
        return projectService.updateProject(project);
    }




    @ApiOperation("删除项目")
    @SysLog("删除项目")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("kube_project_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return projectService.removeProjectById(id);
    }


}