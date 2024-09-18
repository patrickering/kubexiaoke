package com.xiaoke.model.kube.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Domain;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.model.kube.mapper.DomainMapper;
import com.xiaoke.model.kube.service.DomainService;
import com.xiaoke.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 域名 Service
 *
 * @author xiaoke
 * @date 2024-08-18 02:17:05
 */
@Service
@AllArgsConstructor
@Slf4j
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain> implements DomainService {
    private final DomainMapper domainMapper;

    /**
     * 封装共用查询条件
     *
     * @param domain
     * @return
     */
    private QueryWrapper<Domain> baseQuery(Domain domain) {
        QueryWrapper<Domain> query = Wrappers.query();
        query.orderByAsc("id");
        return query;
    }


    /**
     * 分页查询集群域名
     *
     * @param page
     * @param domain
     * @return
     */
    @Override
    public R<IPage> pageDomain(Page page, Domain domain) {
        QueryWrapper<Domain> query = this.baseQuery(domain);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(this.page(page, query));
    }

    /**
     * 获取集群域名列表
     *
     * @param domain
     * @return
     */
    @Override
    public List<Domain> listDomain(Domain domain) {
        QueryWrapper<Domain> query = this.baseQuery(domain);
        if (query == null) {
            return new ArrayList<>();
        }
        return this.list(query);
    }


    /**
     * 通过ID查询集群域名
     *
     * @param id
     * @return
     */
    @Override
    public Domain getDomainById(Integer id) {
        return this.getById(id);
    }

    /**
     * 添加集群域名
     *
     * @param domain
     * @return
     */
    @Override
    public R<Boolean> saveDomain(Domain domain) {
        SysUser user = SecurityUtils.getUser();
        domain.setCreateBy(user.getId());
        log.info("添加集群域名！集群域名:{}", domain);
        Boolean saveFlag = this.save(domain);
        if (saveFlag) {
            return R.ok(Boolean.TRUE, "添加域名成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加域名失败！");
        }
    }

    /**
     * 修改集群域名
     *
     * @param domain
     * @return
     */
    @Override
    public R<Boolean> updateDomain(Domain domain) {
        SysUser user = SecurityUtils.getUser();
        domain.setUpdateBy(user.getId());
        log.info("修改域名！id:{}", domain.getId());
        UpdateWrapper<Domain> updateWrapper = new UpdateWrapper<Domain>();
        updateWrapper.lambda().eq(Domain::getId, domain.getId());
        Boolean updateFlag = this.update(domain, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改域名成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改域名失败！");
        }

    }

    /**
     * 删除集群域名
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeDomainById(Integer id) {
        log.info("删除域名！id:{}", id);
        return R.ok(this.removeById(id), "删除域名成功！");
    }


}