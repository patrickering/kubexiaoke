package com.xiaoke.entity.system.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xiaoke.entity.system.entity.Role;
import com.xiaoke.entity.system.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO extends User {

    @ApiModelProperty(value = "角色Id")
    @TableField(exist = false)
    private Integer roleId;

    /**
     * 角色列表
     */
    @ApiModelProperty(value = "角色列表")
    @TableField(exist = false)
    private List<Role> roleList;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码")
    @TableField(exist = false)
    private String newPassword;


    /**
     * 确认密码
     */
    @ApiModelProperty(value = "确认密码")
    @TableField(exist = false)
    private String passwordConfirm;

    @ApiModelProperty(value = "验证码")
    @TableField(exist = false)
    private String code;

    /**
     * 接收验证码
     */
    @ApiModelProperty(value = "手机验证码")
    @TableField(exist = false)
    private String phoneCode;

    /**
     * 排除id
     */
    @ApiModelProperty(value = "排除id")
    @TableField(exist = false)
    private List<Integer> notId;
}
