package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.AppHost;

import java.util.List;

/**
 * 应用Host Service
 *
 * @author xiaoke
 * @date 2024-08-03 17:10:09
 */
public interface AppHostService extends IService<AppHost> {

    /**
     * 分页查询应用Host
     *
     * @param page
     * @param appHost
     * @return
     */
    R<IPage> pageAppHost(Page page, AppHost appHost);


    /**
     * 获取应用Host列表
     *
     * @param appHost
     * @return
     */
    List<AppHost> listAppHost(AppHost appHost);


    /**
     * 通过ID查询应用Host
     *
     * @param id
     * @return
     */
    AppHost getAppHostById(Integer id);


    /**
     * 添加应用Host
     *
     * @param appHost
     * @return
     */
    R<Boolean> saveAppHost(AppHost appHost);


    /**
     * 修改应用Host
     *
     * @param appHost
     * @return
     */
    R<Boolean> updateAppHost(AppHost appHost);


    /**
     * 删除应用Host
     *
     * @param id
     * @return
     */
    R<Boolean> removeAppHostById(Integer id);


    /**
     * 批量添加应用Host
     *
     * @param app
     */
    void batchSave(AppDTO app);


}