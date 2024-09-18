package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.AppCustomEnv;

import java.util.List;

/**
 * 应用环境变量 Service
 *
 * @author xiaoke
 * @date 2024-08-03 15:45:33
 */
public interface AppCustomEnvService extends IService<AppCustomEnv> {

    /**
     * 分页查询应用环境变量
     *
     * @param page
     * @param appCustomEnv
     * @return
     */
    R<IPage> pageAppCustomEnv(Page page, AppCustomEnv appCustomEnv);


    /**
     * 获取应用环境变量列表
     *
     * @param appCustomEnv
     * @return
     */
    List<AppCustomEnv> listAppCustomEnv(AppCustomEnv appCustomEnv);


    /**
     * 通过ID查询应用环境变量
     *
     * @param id
     * @return
     */
    AppCustomEnv getAppCustomEnvById(Integer id);


    /**
     * 添加应用环境变量
     *
     * @param appCustomEnv
     * @return
     */
    R<Boolean> saveAppCustomEnv(AppCustomEnv appCustomEnv);


    /**
     * 修改应用环境变量
     *
     * @param appCustomEnv
     * @return
     */
    R<Boolean> updateAppCustomEnv(AppCustomEnv appCustomEnv);


    /**
     * 删除应用环境变量
     *
     * @param id
     * @return
     */
    R<Boolean> removeAppCustomEnvById(Integer id);


    /**
     * 批量添加应用环境变量
     *
     * @param app
     */
    void batchSave(AppDTO app);

}