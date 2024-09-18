package com.xiaoke.entity.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntity;
import com.xiaoke.entity.system.entity.Role;
import com.xiaoke.entity.system.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiaoke
 * 用户
 */
@Data
@ApiModel(value = "用户")
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class UserVO extends User {
    /**
     * 权限标识集合
     */
    @ApiModelProperty(value = "权限标识集合")
    @TableField(exist = false)
    private String[] permissions;

    /**
     * 剩余登录次数限制
     */
    @ApiModelProperty(value = "剩余登录次数限制")
    @TableField(exist = false)
    private Integer limitCount;

    /**
     * 限制剩余时间
     */
    @ApiModelProperty(value = "限制剩余时间")
    @TableField(exist = false)
    private String limitTime;

    @ApiModelProperty(value = "角色")
    @TableField(exist = false)
    @BindEntity(entity = Role.class, condition = "this.role_id=role_id")
    private Role role;
}
