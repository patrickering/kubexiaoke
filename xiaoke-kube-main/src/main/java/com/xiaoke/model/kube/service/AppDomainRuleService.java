package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.entity.AppDomainRule;
import java.util.List;

/**
* 应用域名规则 Service
*
*  @author xiaoke
*  @date 2024-08-19 01:09:45
*/
public interface AppDomainRuleService extends IService<AppDomainRule> {

    /**
     * 分页查询应用域名规则
     * @param page
     * @param appDomainRule
     * @return
     */
    R<IPage> pageAppDomainRule(Page page, AppDomainRule appDomainRule);


    /**
     * 获取应用域名规则列表
     * @param appDomainRule
     * @return
     */
    List<AppDomainRule> listAppDomainRule(AppDomainRule appDomainRule);




    /**
     * 通过ID查询应用域名规则
     * @param id
     * @return
     */
     AppDomainRule getAppDomainRuleById(Integer id);


    /**
     * 添加应用域名规则
     * @param appDomainRule
     * @return
     */
    R<Boolean> saveAppDomainRule(AppDomainRule appDomainRule);


    /**
     * 修改应用域名规则
     * @param appDomainRule
     * @return
     */
    R<Boolean> updateAppDomainRule(AppDomainRule appDomainRule);


    /**
     * 删除应用域名规则
     * @param id
     * @return
     */
    R<Boolean> removeAppDomainRuleById(Integer id);





}