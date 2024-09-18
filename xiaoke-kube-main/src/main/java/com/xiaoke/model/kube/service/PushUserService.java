package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.PushUser;
import com.xiaoke.entity.kube.vo.PushUserVO;

import java.util.List;

/**
* 推送接收人 Service
*
*  @author xiaoke
*  @date 2024-08-28 00:33:59
*/
public interface PushUserService extends IService<PushUser> {

    /**
     * 分页查询推送接收人
     * @param page
     * @param pushUser
     * @return
     */
    R<IPage> pagePushUser(Page page, PushUser pushUser);


    /**
     * 获取推送接收人列表
     * @param pushUser
     * @return
     */
    List<PushUserVO> listPushUser(PushUser pushUser);




    /**
     * 通过ID查询推送接收人
     * @param id
     * @return
     */
     PushUser getPushUserById(Integer id);


    /**
     * 添加推送接收人
     * @param pushUser
     * @return
     */
    R<Boolean> savePushUser(PushUser pushUser);


    /**
     * 修改推送接收人
     * @param pushUser
     * @return
     */
    R<Boolean> updatePushUser(PushUser pushUser);


    /**
     * 删除推送接收人
     * @param id
     * @return
     */
    R<Boolean> removePushUserById(Integer id);

    /**
     * 发送异常推送
     *
     * @param namespace
     * @param app
     * @return
     */
    Boolean sendAbnormalPush(String namespace, App app, Integer contentType);



}