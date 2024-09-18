package com.xiaoke.model.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.entity.system.entity.Role;
import com.xiaoke.entity.system.entity.RoleMenu;
import com.xiaoke.model.system.mapper.RoleMapper;
import com.xiaoke.model.system.service.RoleMenuService;
import com.xiaoke.model.system.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private final RoleMenuService roleMenuService;

    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId
     * @return
     */
    @Override
    public List findRolesByUserId(Integer userId) {
        return baseMapper.listRolesByUserId(userId);
    }

    /**
     * 通过角色ID，删除角色,并清空角色菜单缓存
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeRoleById(Integer id) {
        roleMenuService.remove(Wrappers
                .<RoleMenu>update().lambda()
                .eq(RoleMenu::getRoleId, id));
        return this.removeById(id);
    }
}
