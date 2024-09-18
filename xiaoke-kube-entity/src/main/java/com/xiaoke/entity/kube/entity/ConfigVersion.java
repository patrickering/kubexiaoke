package com.xiaoke.entity.kube.entity;


import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

/**
*  配置版本 实体类
*
*  @author xiaoke
*  @date 2024-08-03 16:25:16
*/
@Data
@ApiModel(value = "配置版本")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_config_version")
public class ConfigVersion extends Model<ConfigVersion> {


    /**
    * 主键ID
    */
    @ApiModelProperty(value = "主键ID")
    @TableId
    private Integer id;
    /**
    * 配置ID
    */
    @ApiModelProperty(value = "配置ID")
    private Integer configId;
    /**
    * 版本
    */
    @ApiModelProperty(value = "版本")
    private Integer version;
    /**
    * 内容
    */
    @ApiModelProperty(value = "内容")
    private String content;
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
}