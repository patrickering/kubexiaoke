package com.xiaoke.model.kube.service.impl;

import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import cn.hutool.core.util.StrUtil;
import java.util.List;
import java.util.ArrayList;
import com.xiaoke.entity.kube.entity.AppDomainRule;
import com.xiaoke.model.kube.mapper.AppDomainRuleMapper;
import com.xiaoke.model.kube.service.AppDomainRuleService;

import java.util.stream.Collectors;

/**
* 应用域名规则 Service
*
*  @author xiaoke
*  @date 2024-08-19 01:09:45
*/
@Service
@AllArgsConstructor
@Slf4j
public class AppDomainRuleServiceImpl extends ServiceImpl<AppDomainRuleMapper,AppDomainRule> implements AppDomainRuleService{
    private final AppDomainRuleMapper appDomainRuleMapper;

    /**
     * 封装共用查询条件
     * @param appDomainRule
     * @return
     */
    private QueryWrapper<AppDomainRule> baseQuery(AppDomainRule appDomainRule){
        QueryWrapper<AppDomainRule> query = Wrappers.query();
        if (appDomainRule.getAppDomainId()!=null){
            query.lambda().eq(AppDomainRule::getAppDomainId, appDomainRule.getAppDomainId());
        }
        query.orderByAsc("id");
        return query;
    }


    /**
     * 分页查询应用域名规则
     * @param page
     * @param appDomainRule
     * @return
     */
    @Override
    public R<IPage> pageAppDomainRule(Page page, AppDomainRule appDomainRule){
        QueryWrapper<AppDomainRule> query = this.baseQuery(appDomainRule);
        if(query==null){
            return R.ok(new Page<>());
        }
        return R.ok(this.page(page, query));
    }

    /**
     * 获取应用域名规则列表
     * @param appDomainRule
     * @return
     */
    @Override
    public List<AppDomainRule> listAppDomainRule(AppDomainRule appDomainRule){
        QueryWrapper<AppDomainRule> query = this.baseQuery(appDomainRule);
        if(query==null){
            return new ArrayList<>();
        }
        return this.list(query);
    }





    /**
     * 通过ID查询应用域名规则
     * @param id
     * @return
     */
    @Override
    public AppDomainRule getAppDomainRuleById(Integer id){
        return this.getById(id);
    }

    /**
     * 添加应用域名规则
     * @param appDomainRule
     * @return
     */
    @Override
    public R<Boolean> saveAppDomainRule(AppDomainRule appDomainRule){
        SysUser user = SecurityUtils.getUser();
        appDomainRule.setCreateBy(user.getId());
        log.info("添加应用域名规则！应用域名规则:{}",appDomainRule);
        Boolean saveFlag = this.save(appDomainRule);
        if(saveFlag){
            return R.ok(Boolean.TRUE,"添加应用域名规则成功！");
        }else{
            return R.ok(Boolean.FALSE,"添加应用域名规则失败！");
        }
    }

    /**
     * 修改应用域名规则
     * @param appDomainRule
     * @return
     */
    @Override
    public R<Boolean> updateAppDomainRule(AppDomainRule appDomainRule){
        SysUser user = SecurityUtils.getUser();
        appDomainRule.setUpdateBy(user.getId());
        log.info("修改应用域名规则！id:{}",appDomainRule.getId());
        UpdateWrapper<AppDomainRule> updateWrapper = new UpdateWrapper<AppDomainRule>();
        updateWrapper.lambda().set(AppDomainRule::getAppDomainId,appDomainRule.getAppDomainId());
        updateWrapper.lambda().set(AppDomainRule::getAppId,appDomainRule.getAppId());
        updateWrapper.lambda().set(AppDomainRule::getPort,appDomainRule.getPort());
        updateWrapper.lambda().eq(AppDomainRule::getId,appDomainRule.getId());
        Boolean updateFlag = this.update(appDomainRule,updateWrapper);
        if(updateFlag){
            return R.ok(Boolean.TRUE,"修改应用域名规则成功！");
        }else{
            return R.ok(Boolean.FALSE,"修改应用域名规则失败！");
        }

    }

    /**
     * 删除应用域名规则
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeAppDomainRuleById(Integer id){
        log.info("删除应用域名规则！id:{}",id);
        return R.ok(this.removeById(id),"删除应用域名规则成功！");
    }




}