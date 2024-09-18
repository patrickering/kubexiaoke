package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.AppService;
import java.util.List;

/**
* 应用Service Service
*
*  @author xiaoke
*  @date 2024-08-03 16:39:45
*/
public interface AppServiceService extends IService<AppService> {

    /**
     * 分页查询应用Service
     * @param page
     * @param appService
     * @return
     */
    R<IPage> pageAppService(Page page, AppService appService);


    /**
     * 获取应用Service列表
     * @param appService
     * @return
     */
    List<AppService> listAppService(AppService appService);




    /**
     * 通过ID查询应用Service
     * @param id
     * @return
     */
     AppService getAppServiceById(Integer id);


    /**
     * 添加应用Service
     * @param appService
     * @return
     */
    R<Boolean> saveAppService(AppService appService);


    /**
     * 修改应用Service
     * @param appService
     * @return
     */
    R<Boolean> updateAppService(AppService appService);


    /**
     * 删除应用Service
     * @param id
     * @return
     */
    R<Boolean> removeAppServiceById(Integer id);


    /**
     * 批量保存
     * @param app
     */
    void batchSave(AppDTO app);


}