package com.xiaoke.model.kube.controller;

import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.xiaoke.annotation.permission.Permission;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.entity.kube.entity.Template;
import com.xiaoke.model.kube.service.TemplateService;
/**
* 模板 Controller
*
*  @author xiaoke
*  @date 2024-08-17 21:23:43
*/
@Api(value = "template", tags = "模板")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/template")
public class TemplateController {

    private final TemplateService templateService;


    @ApiOperation("分页查询模板")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true),
        @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/page")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,filedName="current", notNull = true, msg = "缺少current参数！"),
        @ValidateFiled(index = 0,filedName="size",  notNull = true, msg = "缺少size参数")
    })
    @Permission("kube_template_view")
    public R<IPage> page(Page page, Template template) {
        return templateService.pageTemplate(page, template);
    }



	@ApiOperation("获取模板列表")
    @GetMapping("/list")
    @Permission("kube_template_view")
    public R list(Template template) {
        return R.ok(templateService.listTemplate(template));
    }


    @ApiOperation("通过ID查询模板")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_template_view")
    public R getById(@ApiParam(name = "id", value = "模板ID", required = true) @PathVariable Integer id) {
        return R.ok(templateService.getTemplateById(id));
    }


    @ApiOperation("添加模板")
    @SysLog("添加模板")
    @PostMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0,notNull = true,maxLen = 100,filedName = "name", msg = "[名称]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "type", msg = "[类型]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "content", msg = "[内容]验证未通过！"),
    })
    @Permission("kube_template_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody Template template) {
        return templateService.saveTemplate(template);
    }


    @ApiOperation("修改模板")
    @SysLog("修改模板")
    @PutMapping
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, filedName ="id", msg = "缺少id参数！"),
        @ValidateFiled(index = 0,notNull = true,maxLen = 100,filedName = "name", msg = "[名称]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "type", msg = "[类型]验证未通过！"),
        @ValidateFiled(index = 0,notNull = true,filedName = "content", msg = "[内容]验证未通过！")
    })
    @Permission("kube_template_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody Template template) {
        return templateService.updateTemplate(template);
    }


    @ApiOperation("删除模板")
    @SysLog("删除模板")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {
        @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("kube_template_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return templateService.removeTemplateById(id);
    }






}