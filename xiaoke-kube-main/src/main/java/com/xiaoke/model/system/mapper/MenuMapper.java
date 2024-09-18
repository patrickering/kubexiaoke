

package com.xiaoke.model.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoke.entity.system.entity.Menu;
import com.xiaoke.entity.system.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author xiaoke
 * @since 2017-10-29
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 通过角色编号查询菜单
     *
     * @param roleIdList 角色ID
     * @return
     */
    List<MenuVO> listMenusByRoleId(List<Integer> roleIdList);

    /**
     * 通过角色ID查询权限
     *
     * @param roleIdList
     * @return
     */
    List<String> listPermissionsByRoleIds(List<Integer> roleIdList);
}
