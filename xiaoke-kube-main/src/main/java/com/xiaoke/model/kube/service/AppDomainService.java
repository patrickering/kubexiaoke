package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.dto.AppDomainDTO;
import com.xiaoke.entity.kube.entity.AppDomain;
import java.util.List;
import com.xiaoke.entity.kube.vo.AppDomainVO;

/**
* 应用域名 Service
*
*  @author xiaoke
*  @date 2024-08-19 00:39:43
*/
public interface AppDomainService extends IService<AppDomain> {

    /**
     * 分页查询应用域名
     * @param page
     * @param appDomain
     * @return
     */
    R<IPage> pageAppDomain(Page page, AppDomain appDomain);


    /**
     * 获取应用域名列表
     * @param appDomain
     * @return
     */
    List<AppDomainVO> listAppDomain(AppDomain appDomain);




    /**
     * 通过ID查询应用域名
     * @param id
     * @return
     */
     AppDomainVO getAppDomainById(Integer id);


    /**
     * 添加应用域名
     * @param appDomain
     * @return
     */
    R<Boolean> saveAppDomain(AppDomainDTO appDomain) throws Exception;


    /**
     * 修改应用域名
     * @param appDomain
     * @return
     */
    R<Boolean> updateAppDomain(AppDomain appDomain);


    /**
     * 删除应用域名
     * @param id
     * @return
     */
    R<Boolean> removeAppDomainById(Integer id);

}