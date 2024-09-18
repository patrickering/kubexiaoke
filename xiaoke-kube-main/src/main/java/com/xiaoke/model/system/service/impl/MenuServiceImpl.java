

package com.xiaoke.model.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.enums.MenuTypeEnum;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.dto.MenuTree;
import com.xiaoke.entity.system.entity.Menu;
import com.xiaoke.entity.system.entity.RoleMenu;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.entity.system.vo.MenuVO;
import com.xiaoke.model.system.mapper.MenuMapper;
import com.xiaoke.model.system.mapper.RoleMenuMapper;
import com.xiaoke.model.system.service.MenuService;
import com.xiaoke.utils.SecurityUtils;
import com.xiaoke.utils.TreeUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author xiaoke
 * @since 2017-10-29
 */
@Service
@AllArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public List<MenuVO> findMenuByRoleId(List<Integer> roleIdList) {
        return baseMapper.listMenusByRoleId(roleIdList);
    }

    /**
     * 通过角色列表查询权限
     *
     * @param roleIdList
     * @return
     */
    @Override
    public List<String> findPermissionByRoleIdList(List<Integer> roleIdList) {
        return baseMapper.listPermissionsByRoleIds(roleIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R removeMenuById(Integer id) {
        // 查询父节点为当前节点的节点
        List<Menu> menuList = this.list(Wrappers.<Menu>query()
                .lambda().eq(Menu::getParentId, id));
        if (CollUtil.isNotEmpty(menuList)) {
            return R.failed("菜单含有下级不能删除");
        }

        roleMenuMapper.delete(Wrappers.<RoleMenu>query()
                .lambda().eq(RoleMenu::getMenuId, id));
        //删除当前菜单及其子菜单
        return R.ok(this.removeById(id));
    }

    @Override
    public Boolean updateMenuById(Menu menu) {
        return this.updateById(menu);
    }

    /**
     * 构建树查询
     * 1. 不是懒加载情况，查询全部
     * 2. 是懒加载，根据parentId 查询
     * 2.1 父节点为空，则查询ID -1
     *
     * @param lazy     是否是懒加载
     * @param parentId 父节点ID
     * @return
     */
    @Override
    public List<MenuTree> treeMenu(boolean lazy, Integer parentId, MenuVO menuVO) {
        //获取用户角色
        SysUser user = SecurityUtils.getUser();
        List<Integer> collect = null;

        List<Integer> roleIdList = new ArrayList<>();
        if (user.getRoleId() != null) {
            roleIdList.add(user.getRoleId());
        }
        if (!roleIdList.contains(1) && !roleIdList.contains(2)) {
            collect = baseMapper.listMenusByRoleId(roleIdList).stream().map(MenuVO::getMenuId).collect(Collectors.toList());
        }

        if (!lazy) {
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = Wrappers.<Menu>lambdaQuery().orderByAsc(Menu::getSort);
            if (StrUtil.isNotEmpty(menuVO.getName())) {
                menuLambdaQueryWrapper.like(Menu::getName, menuVO.getName());
            }
            List<Menu> menu = baseMapper.selectList(menuLambdaQueryWrapper);
            if (roleIdList.contains(1) || roleIdList.contains(2)) {
                return TreeUtil.buildTree(menu, Constant.MENU_TREE_ROOT_ID);
            }
            List<Menu> menuList = new ArrayList<>();
            for (Menu sysMenu : menu) {
                if (collect.contains(sysMenu.getMenuId())) {
                    menuList.add(sysMenu);
                }
            }
            return TreeUtil.buildTree(menuList, Constant.MENU_TREE_ROOT_ID);
        }

        Integer parent = parentId == null ? Constant.MENU_TREE_ROOT_ID : parentId;
        final LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = Wrappers.<Menu>lambdaQuery().eq(Menu::getParentId, parent).orderByAsc(Menu::getSort);
        if (StrUtil.isNotEmpty(menuVO.getName())) {
            menuLambdaQueryWrapper.like(Menu::getName, menuVO.getName());
        }
        List<Menu> menu = baseMapper.selectList(menuLambdaQueryWrapper);
        if (roleIdList.contains(1) || roleIdList.contains(2)) {
            return TreeUtil.buildTree(menu, parent);
        }

        List<Menu> menuList = new ArrayList<>();
        for (Menu sysMenu : menu) {
            if (collect.contains(sysMenu.getMenuId())) {
                menuList.add(sysMenu);
            }
        }
        return TreeUtil.buildTree(menuList, parent);
    }

    /**
     * 查询菜单
     *
     * @param all      全部菜单
     * @param type     类型
     * @param parentId 父节点ID
     * @return
     */
    @Override
    public List<MenuTree> filterMenu(Set<MenuVO> all, String type, Integer parentId) {
        List<MenuTree> menuTreeList = all.stream()
                .filter(menuTypePredicate(type))
                .map(MenuTree::new)
                .sorted(Comparator.comparingInt(MenuTree::getSort))
                .collect(Collectors.toList());
        Integer parent = parentId == null ? Constant.MENU_TREE_ROOT_ID : parentId;
        return TreeUtil.build(menuTreeList, parent);
    }

    /**
     * menu 类型断言
     *
     * @param type 类型
     * @return Predicate
     */
    private Predicate<MenuVO> menuTypePredicate(String type) {
        return vo -> {
            if (MenuTypeEnum.TOP_MENU.getDescription().equals(type)) {
                return MenuTypeEnum.TOP_MENU.getType().equals(vo.getType());
            }
            // 其他查询 左侧 + 顶部
            return !MenuTypeEnum.BUTTON.getType().equals(vo.getType());
        };
    }
}
