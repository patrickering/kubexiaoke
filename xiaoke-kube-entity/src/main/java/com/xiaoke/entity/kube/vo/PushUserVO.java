package com.xiaoke.entity.kube.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntity;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.kube.entity.PushUser;
import com.xiaoke.entity.system.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 推送接收人 VO
 *
 * @author xiaoke
 * @date 2024-08-28 00:33:59
 */
@Data
@ApiModel(value = "推送接收人")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_push_user")
public class PushUserVO extends PushUser {

    /**
     * 用户
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "用户")
    @BindEntity(entity= User.class, condition="user_id=id")
    private User user;
}