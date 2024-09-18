

package com.xiaoke.model.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.dto.MenuTree;
import com.xiaoke.entity.system.entity.Menu;
import com.xiaoke.entity.system.vo.MenuVO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author xiaoke
 * @since 2017-10-29
 */
public interface MenuService extends IService<Menu> {
    /**
     * 通过角色编号查询URL 权限
     *
     * @param roleIdList 角色ID
     * @return 菜单列表
     */
    List<MenuVO> findMenuByRoleId(List<Integer> roleIdList);

    /**
     * 通过角色列表查询权限
     * @param roleIdList
     * @return
     */
    List<String> findPermissionByRoleIdList(List<Integer> roleIdList);

    /**
     * 级联删除菜单
     *
     * @param id 菜单ID
     * @return 成功、失败
     */
    R removeMenuById(Integer id);

    /**
     * 更新菜单信息
     *
     * @param menu 菜单信息
     * @return 成功、失败
     */
    Boolean updateMenuById(Menu menu);

    /**
     * 构建树
     *
     * @param lazy     是否是懒加载
     * @param parentId 父节点ID
     * @return
     */
    List<MenuTree> treeMenu(boolean lazy, Integer parentId, MenuVO menuVO);

    /**
     * 查询菜单
     *
     * @param menuVOSet
     * @param parentId
     * @return
     */
    List<MenuTree> filterMenu(Set<MenuVO> menuVOSet, String type, Integer parentId);
}
