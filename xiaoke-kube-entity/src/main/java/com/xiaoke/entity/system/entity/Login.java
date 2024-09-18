
package com.xiaoke.entity.system.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典表
 *
 * @author xiaoke
 * @date 2019/03/19
 */
@Data
@ApiModel(value = "登录实体类")
@EqualsAndHashCode(callSuper = true)
public class Login extends Model<Login> {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 图形验证码
     */
    @ApiModelProperty(value = "图形验证码")
    private String captcha;
    /**
     * 图形验证码标识
     */
    @ApiModelProperty(value = "图形验证码标识")
    private String randomStr;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String code;
    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private String source;
    /**
     * 微信小程序用户信息加密字段
     */
    @ApiModelProperty(value = "微信小程序用户信息加密字段")
    private String encryptedData;
    /**
     * 微信小程序用户信息加密字段
     */
    @ApiModelProperty(value = "微信小程序用户信息加密字段")
    private String ivStr;

}
