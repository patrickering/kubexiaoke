package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.dto.AppConfigDTO;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.AppConfig;
import java.util.List;

/**
* 应用配置 Service
*
*  @author xiaoke
*  @date 2024-08-03 16:31:30
*/
public interface AppConfigService extends IService<AppConfig> {

    /**
     * 分页查询应用配置
     * @param page
     * @param appConfig
     * @return
     */
    R<IPage> pageAppConfig(Page page, AppConfig appConfig);


    /**
     * 获取应用配置列表
     * @param appConfig
     * @return
     */
    List<AppConfig> listAppConfig(AppConfig appConfig);




    /**
     * 通过ID查询应用配置
     * @param id
     * @return
     */
     AppConfig getAppConfigById(Integer id);


    /**
     * 添加应用配置
     * @param appConfig
     * @return
     */
    R<Boolean> saveAppConfig(AppConfig appConfig);


    /**
     * 修改应用配置
     * @param appConfig
     * @return
     */
    R<Boolean> updateAppConfig(AppConfig appConfig);


    /**
     * 删除应用配置
     * @param id
     * @return
     */
    R<Boolean> removeAppConfigById(Integer id);

    /**
     *批量保存
     * @param app
     */
    void batchSave(AppDTO app);

    /**
     * 根据版本查询
     * @param versionId
     * @return
     */
    R getByVersion(Integer versionId);


    /**
     * 更新应用版本
     *
     * @param configApp
     */
    void updateAppVersion(AppConfigDTO configApp) throws Exception;
}