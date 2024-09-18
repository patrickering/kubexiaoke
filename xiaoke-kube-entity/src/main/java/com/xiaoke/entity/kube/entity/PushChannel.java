package com.xiaoke.entity.kube.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 推送渠道 实体类
 *
 * @author chendengwen
 * @date 2019-12-23 23:40:14
 */
@Data
@TableName("kube_push_channel")
@EqualsAndHashCode(callSuper = true)
public class PushChannel extends Model<PushChannel> {

    @TableId
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    /**
     * 推送ID
     */
    @ApiModelProperty(value = "推送ID")
    private Integer pushId;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;

    /**
     * 名称
     */
    @ApiModelProperty(value = "类型")
    private String name;

    /**
     * 参数
     */
    @ApiModelProperty(value = "参数")
    private String params;

    /**
     * 权重
     */
    @ApiModelProperty(value = "类型")
    private Integer weight;



    /**
     * 是否开启
     */
    @ApiModelProperty(value = "是否开启")
    private Boolean open;

}