package com.xiaoke.model.kube.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Domain;
import com.xiaoke.model.kube.service.DomainService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 域名 Controller
 *
 * @author xiaoke
 * @date 2024-08-18 23:43:43
 */
@Api(value = "Domain", tags = "域名")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/domain")
public class DomainController {

    private final DomainService domainService;


    @ApiOperation("分页查询集群域名")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_domain_view")
    public R<IPage> page(Page page, Domain domain) {
        return domainService.pageDomain(page, domain);
    }


    @ApiOperation("获取集群域名列表")
    @GetMapping("/list")
    @Permission("kube_domain_view")
    public R list(Domain domain) {
        return R.ok(domainService.listDomain(domain));
    }


    @ApiOperation("通过ID查询集群域名")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_domain_view")
    public R getById(@ApiParam(name = "id", value = "集群域名ID", required = true) @PathVariable Integer id) {
        return R.ok(domainService.getDomainById(id));
    }


    @ApiOperation("添加集群域名")
    @SysLog("添加集群域名")
    @PostMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, maxLen = 255, filedName = "domain", msg = "[域名]验证未通过！"),})
    @Permission("kube_domain_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody Domain domain) {
        return domainService.saveDomain(domain);
    }


    @ApiOperation("修改集群域名")
    @SysLog("修改集群域名")
    @PutMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！"), @ValidateFiled(index = 0, notNull = true, maxLen = 255, filedName = "domain", msg = "[域名]验证未通过！")})
    @Permission("kube_domain_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody Domain domain) {
        return domainService.updateDomain(domain);
    }


    @ApiOperation("删除集群域名")
    @SysLog("删除集群域名")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_domain_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return domainService.removeDomainById(id);
    }


}