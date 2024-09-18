package com.xiaoke.entity.kube.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 推送 实体类
 *
 * @author chendengwen
 * @date 2019-12-23 23:40:14
 */
@Data
@TableName("kube_push")
@EqualsAndHashCode(callSuper = true)
public class Push extends Model<Push> {

    @TableId
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 类型（email=邮件，sms=短信，phone=电话，wx=微信）
     */
    @ApiModelProperty(value = "类型（email=邮件，sms=短信，phone=电话，wx=微信）")
    private String type;

    /**
     * 是否开启
     */
    @ApiModelProperty(value = "是否开启")
    private Boolean open;

}