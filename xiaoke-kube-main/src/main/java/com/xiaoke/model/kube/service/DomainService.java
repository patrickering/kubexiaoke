package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Domain;

import java.util.List;

/**
 * 集群域名 Service
 *
 * @author xiaoke
 * @date 2024-08-18 02:17:05
 */
public interface DomainService extends IService<Domain> {

    /**
     * 分页查询集群域名
     *
     * @param page
     * @param domain
     * @return
     */
    R<IPage> pageDomain(Page page, Domain domain);


    /**
     * 获取集群域名列表
     *
     * @param domain
     * @return
     */
    List<Domain> listDomain(Domain domain);


    /**
     * 通过ID查询集群域名
     *
     * @param id
     * @return
     */
    Domain getDomainById(Integer id);


    /**
     * 添加集群域名
     *
     * @param domain
     * @return
     */
    R<Boolean> saveDomain(Domain domain);


    /**
     * 修改集群域名
     *
     * @param domain
     * @return
     */
    R<Boolean> updateDomain(Domain domain);


    /**
     * 删除集群域名
     *
     * @param id
     * @return
     */
    R<Boolean> removeDomainById(Integer id);


}