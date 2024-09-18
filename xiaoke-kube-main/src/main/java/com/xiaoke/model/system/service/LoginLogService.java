package com.xiaoke.model.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.LoginLog;


/**
 * 登录日志 Service
 *
 * @author xiaoke
 * @date 2022-01-23 15:30:21
 */
public interface LoginLogService extends IService<LoginLog> {

    /**
     * 分页查询登录日志
     *
     * @param page
     * @param loginLog
     * @return
     */
    R<IPage> pageLoginLog(Page page, LoginLog loginLog);


    /**
     * 获取登录日志列表
     *
     * @param loginLog
     * @return
     */
    R listLoginLog(LoginLog loginLog);


    /**
     * 通过ID查询登录日志
     *
     * @param id
     * @return
     */
    R getLoginLogById(Integer id);


    /**
     * 添加登录日志
     *
     * @param loginLog
     * @return
     */
    R<Boolean> saveLoginLog(LoginLog loginLog);


    /**
     * 修改登录日志
     *
     * @param loginLog
     * @return
     */
    R<Boolean> updateLoginLog(LoginLog loginLog);


    /**
     * 删除登录日志
     *
     * @param id
     * @return
     */
    R<Boolean> removeLoginLogById(Integer id);


}