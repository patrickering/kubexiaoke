package com.xiaoke.model.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoke.entity.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId
     * @return
     */
    List<Role> listRolesByUserId(Integer userId);
}
