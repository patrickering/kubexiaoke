

package com.xiaoke.model.system.controller;

import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.Menu;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.entity.system.vo.MenuVO;
import com.xiaoke.model.system.service.MenuService;
import com.xiaoke.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaoke
 * @date 2017/10/31
 */
@RestController
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.system.prefix}/menu")
@Api(value = "menu", tags = "菜单")
public class MenuController {
    private final MenuService menuService;

    /**
     * 返回当前用户的树形菜单集合
     *
     * @param type     类型
     * @param parentId 父节点ID
     * @return 当前用户的树形菜单
     */
    @ApiOperation("当前用户树形菜单")
    @GetMapping
    public R getUserMenu(String type, Integer parentId) {
        // 获取符合条件的菜单
        Set<MenuVO> all = new HashSet<>();
        SysUser user = SecurityUtils.getUser();
        List<Integer> roleIdList = new ArrayList<>();
        roleIdList.add(user.getRoleId());
        List<MenuVO> menuList = menuService.findMenuByRoleId(roleIdList);
        all.addAll(menuList);
        return R.ok(menuService.filterMenu(all, type, parentId));
    }

    /**
     * 返回树形菜单集合
     *
     * @param lazy     是否是懒加载
     * @param parentId 父节点ID
     * @return 树形菜单
     */
    @ApiOperation("树形菜单")
    @GetMapping(value = "/tree")
    public R getTree(boolean lazy, Integer parentId, MenuVO menuVO) {
        return R.ok(menuService.treeMenu(lazy, parentId, menuVO));
    }

    /**
     * 返回角色的菜单集合
     *
     * @param roleId 角色ID
     * @return 属性集合
     */
    @ApiOperation("角色菜单")
    @GetMapping("/tree/{roleId}")
    public R getRoleTree(@PathVariable Integer roleId) {
        List<Integer> idList = new ArrayList<>();
        idList.add(roleId);
        return R.ok(menuService.findMenuByRoleId(idList).stream().map(MenuVO::getMenuId).collect(Collectors.toList()));
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param id 菜单ID
     * @return 菜单详细信息
     */
    @ApiOperation("通过ID查询菜单")
    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id) {
        return R.ok(menuService.getById(id));
    }

    /**
     * 新增菜单
     *
     * @param menu 菜单信息
     * @return success/false
     */
    @ApiOperation("新增菜单")
    @SysLog("新增菜单")
    @PostMapping
    @Permission("sys_menu_add")
    public R save(@Valid @RequestBody Menu menu) {
        return R.ok(menuService.save(menu));
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return success/false
     */
    @ApiOperation("删除菜单")
    @SysLog("删除菜单")
    @DeleteMapping("/{id}")
    @Permission("sys_menu_del")
    public R removeById(@PathVariable Integer id) {
        return menuService.removeMenuById(id);
    }

    /**
     * 更新菜单
     *
     * @param menu
     * @return
     */
    @SysLog("更新菜单")
    @ApiOperation("更新菜单")
    @PutMapping
    @Permission("sys_menu_edit")
    public R update(@Valid @RequestBody Menu menu) {
        return R.ok(menuService.updateMenuById(menu));
    }

}
