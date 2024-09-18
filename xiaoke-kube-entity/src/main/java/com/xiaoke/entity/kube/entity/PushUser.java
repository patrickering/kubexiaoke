package com.xiaoke.entity.kube.entity;


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
 * 推送接收人 实体类
 *
 * @author xiaoke
 * @date 2024-08-28 00:33:59
 */
@Data
@ApiModel(value = "推送接收人")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_push_user")
public class PushUser extends Model<PushUser> {


    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId
    private Integer id;
    /**
     * 命名空间
     */
    @ApiModelProperty(value = "命名空间")
    private String namespace;
    /**
     * 项目用户ID
     */
    @ApiModelProperty(value = "项目用户ID")
    private Integer projectUserId;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Integer userId;
    /**
     * 推送类型
     */
    @ApiModelProperty(value = "推送类型")
    private String pushType;
    /**
     * 内容类型（0=异常，1=操作，2=全部）
     */
    @ApiModelProperty(value = "内容类型（0=异常，1=操作，2=全部）")
    private Integer contentType;
    /**
     * 是否删除(0=false，1=true)
     */
    @ApiModelProperty(value = "是否删除(0=false，1=true)")
    @TableLogic
    private String delFlag;
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
    private LocalDateTime createTime;
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
    private LocalDateTime updateTime;
}