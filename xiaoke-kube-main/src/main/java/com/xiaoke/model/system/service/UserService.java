package com.xiaoke.model.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.dto.UserDTO;
import com.xiaoke.entity.system.entity.User;

/**
 * 用户 Service
 *
 * @author xiaoke
 */
public interface UserService extends IService<User> {
    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
    IPage pageUser(Page page, UserDTO userDTO);

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    R updateUser(UserDTO user);

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    R<Boolean> saveUser(UserDTO user);

    /**
     * 解除用户限制
     *
     * @param username
     * @return
     */
    R relieveLimit(String username);

    /**
     * 修改用户openId
     *
     * @param openId
     * @return
     */
    R<Boolean> updateOpenId(String openId);
}
