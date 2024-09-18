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
 * 集群域名 实体类
 *
 * @author xiaoke
 * @date 2024-08-18 02:17:05
 */
@Data
@ApiModel(value = "集群域名")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_domain")
public class Domain extends Model<Domain> {


    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId
    private Integer id;
    /**
     * 域名
     */
    @ApiModelProperty(value = "域名")
    private String domain;
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