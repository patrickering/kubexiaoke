package com.xiaoke.model.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.Role;
import com.xiaoke.entity.system.vo.RoleVo;
import com.xiaoke.model.system.service.RoleMenuService;
import com.xiaoke.model.system.service.RoleService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色 Controller
 *
 * @author xiaoke
 */
@AllArgsConstructor
@RestController
@RequestMapping("${controller.prefix}/${controller.system.prefix}/role")
public class RoleController {
    private final RoleService roleService;
    private final RoleMenuService roleMenuService;

    /**
     * 通过ID查询角色信息
     *
     * @param id ID
     * @return 角色信息
     */
    @ApiOperation("通过ID查询角色")
    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id) {
        return R.ok(roleService.getById(id));
    }

    /**
     * 添加角色
     *
     * @param role 角色信息
     * @return success、false
     */
    @SysLog("添加角色")
    @ApiOperation("添加角色")
    @PostMapping
    @Permission("sys_role_add")
    public R save(@Valid @RequestBody Role role) {
        return R.ok(roleService.save(role));
    }

    /**
     * 修改角色
     *
     * @param role 角色信息
     * @return success/false
     */
    @ApiOperation("修改角色")
    @SysLog("修改角色")
    @PutMapping
    @Permission("sys_role_edit")
    public R update(@Valid @RequestBody Role role) {
        return R.ok(roleService.updateById(role));
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @ApiOperation("删除角色")
    @SysLog("删除角色")
    @DeleteMapping("/{id}")
    @Permission("sys_role_del")
    public R removeById(@PathVariable Integer id) {
        return R.ok(roleService.removeRoleById(id));
    }

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    @ApiOperation("获取角色列表")
    @GetMapping("/list")
    public R listRoles(Role role) {
        return R.ok(roleService.list(Wrappers.query(role)));
    }

    /**
     * 分页查询角色信息
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @ApiOperation("分页查询角色")
    @GetMapping("/page")
    public R<Page<Role>> getRolePage(Page page, String name) {
        QueryWrapper<Role> query = Wrappers.query();
        if (StrUtil.isNotEmpty(name)) {
            query = query.like("role_name", name);
        }
        return R.ok(roleService.page(page, query));
    }

    /**
     * 更新角色菜单
     *
     * @param roleVo 角色对象
     * @return success、false
     */
    @ApiOperation("更新角色菜单")
    @SysLog("更新角色菜单")
    @PutMapping("/menu")
    @Permission("sys_role_perm")
    public R saveRoleMenus(@RequestBody RoleVo roleVo) {
        Role role = roleService.getById(roleVo.getRoleId());
        return R.ok(roleMenuService.saveRoleMenus(role.getRoleCode()
                , roleVo.getRoleId(), roleVo.getMenuIds()));
    }

    /**
     * 通过角色ID 查询角色列表
     *
     * @param roleIdList 角色ID
     * @return
     */
    @ApiOperation("通过角色ID 查询角色")
    @PostMapping("/getRoleList")
    public R getRoleList(@RequestBody List<String> roleIdList) {
        return R.ok(roleService.listByIds(roleIdList));
    }
}
