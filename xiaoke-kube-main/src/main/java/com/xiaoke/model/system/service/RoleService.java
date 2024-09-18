package com.xiaoke.model.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.system.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId
     * @return
     */
    List<Role> findRolesByUserId(Integer userId);

    /**
     * 通过角色ID，删除角色
     *
     * @param id
     * @return
     */
    Boolean removeRoleById(Integer id);
}
