package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.AppPort;

import java.util.List;

/**
 * 应用端口 Service
 *
 * @author xiaoke
 * @date 2024-08-03 16:44:18
 */
public interface AppPortService extends IService<AppPort> {

    /**
     * 分页查询应用端口
     *
     * @param page
     * @param appPort
     * @return
     */
    R<IPage> pageAppPort(Page page, AppPort appPort);


    /**
     * 获取应用端口列表
     *
     * @param appPort
     * @return
     */
    List<AppPort> listAppPort(AppPort appPort);


    /**
     * 通过ID查询应用端口
     *
     * @param id
     * @return
     */
    AppPort getAppPortById(Integer id);


    /**
     * 添加应用端口
     *
     * @param appPort
     * @return
     */
    R<Boolean> saveAppPort(AppPort appPort);


    /**
     * 修改应用端口
     *
     * @param appPort
     * @return
     */
    R<Boolean> updateAppPort(AppPort appPort);


    /**
     * 删除应用端口
     *
     * @param id
     * @return
     */
    R<Boolean> removeAppPortById(Integer id);

}