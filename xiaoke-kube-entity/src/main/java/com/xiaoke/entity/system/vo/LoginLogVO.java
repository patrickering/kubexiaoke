package com.xiaoke.entity.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.system.entity.LoginLog;
import com.xiaoke.entity.system.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 登录日志 VO
 *
 * @author xiaoke
 * @date 2022-01-23 15:30:21
 */
@Data
@ApiModel(value = "登录日志VO")
@EqualsAndHashCode(callSuper = true)
@TableName("sys_login_log")
public class LoginLogVO extends LoginLog {

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity = User.class, field = "name", condition = "create_by=id")
    private String createName;
}