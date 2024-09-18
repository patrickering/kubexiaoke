

package com.xiaoke.model.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.system.entity.RoleMenu;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author xiaoke
 * @since 2017-10-29
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 更新角色菜单
     *
     * @param role
     * @param roleId  角色
     * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return
     */
    Boolean saveRoleMenus(String role, Integer roleId, String menuIds);
}
