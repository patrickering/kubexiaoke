package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.AppStorage;
import java.util.List;

/**
* 应用存储 Service
*
*  @author xiaoke
*  @date 2024-08-03 17:06:23
*/
public interface AppStorageService extends IService<AppStorage> {

    /**
     * 分页查询应用存储
     * @param page
     * @param appStorage
     * @return
     */
    R<IPage> pageAppStorage(Page page, AppStorage appStorage);


    /**
     * 获取应用存储列表
     * @param appStorage
     * @return
     */
    List<AppStorage> listAppStorage(AppStorage appStorage);




    /**
     * 通过ID查询应用存储
     * @param id
     * @return
     */
     AppStorage getAppStorageById(Integer id);


    /**
     * 添加应用存储
     * @param appStorage
     * @return
     */
    R<Boolean> saveAppStorage(AppStorage appStorage);


    /**
     * 修改应用存储
     * @param appStorage
     * @return
     */
    R<Boolean> updateAppStorage(AppStorage appStorage);


    /**
     * 删除应用存储
     * @param id
     * @return
     */
    R<Boolean> removeAppStorageById(Integer id);



    /**
     * 批量保存
     * @param app
     */
    void batchSave(AppDTO app);

}