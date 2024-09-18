package com.xiaoke.entity.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author xiaoke
 * 用户
 */
@Data
@ApiModel(value = "用户")
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends User {
    /**
     * 权限标识集合
     */
    @ApiModelProperty(value = "权限标识集合")
    private String[] permissions;

    /**
     * 角色
     */
    @ApiModelProperty(value = "角色")
    private Role role;
}
