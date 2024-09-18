package com.xiaoke.entity.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author xiaoke
 * 用户
 */
@Data
@ApiModel(value = "用户")
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends Model<User> {
    @TableId
    @ApiModelProperty(value = "主键ID")
    private Integer id;
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 手机
     */
    @ApiModelProperty(value = "手机")
    private String phone;

    /**
     * 微信openid
     */
    @ApiModelProperty(value = "微信openid")
    private String wxOpenid;

    /**
     * 微信小程序openid
     */
    @ApiModelProperty(value = "微信小程序openid")
    private String miniOpenid;

    /**
     * 最后登陆ip
     */
    @ApiModelProperty(value = "最后登陆ip")
    private String loginIp;
    /**
     * 最后登陆时间
     */
    @ApiModelProperty(value = "最后登陆时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDate;
    /**
     * 是否可登陆
     */
    @ApiModelProperty(value = "是否可登陆")
    private String loginFlag;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Integer updateBy;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    /**
     * 0-正常，1-删除
     */
    @TableLogic
    @ApiModelProperty(value = "删除标记,1:已删除,0:正常")
    private String delFlag;


}
