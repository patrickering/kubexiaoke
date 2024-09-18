package com.xiaoke.entity.kube.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

/**
*  弹性伸缩 实体类
*
*  @author xiaoke
*  @date 2024-08-09 16:29:23
*/
@Data
@ApiModel(value = "弹性伸缩")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_stretch")
public class AppStretch extends Model<AppStretch> {


    /**
    * 主键ID
    */
    @ApiModelProperty(value = "主键ID")
    @TableId
    private Integer id;
    /**
    * 应用ID
    */
    @ApiModelProperty(value = "应用ID")
    private Integer appId;
    /**
    * 最小副本数
    */
    @ApiModelProperty(value = "最小副本数")
    private Integer minReplicas;
    /**
    * 最大副本数
    */
    @ApiModelProperty(value = "最大副本数")
    private Integer maxReplicas;
    /**
    * 是否开启cpu
    */
    @ApiModelProperty(value = "是否开启cpu")
    private Boolean cpu;
    /**
    * cpu数值类型（value=数值，proportion=比例）
    */
    @ApiModelProperty(value = "cpu数值类型（value=数值，proportion=比例）")
    private String cpuValueType;
    /**
    * cpu数值
    */
    @ApiModelProperty(value = "cpu数值")
    private Double cpuValue;
    /**
    * 是否开启内存
    */
    @ApiModelProperty(value = "是否开启内存")
    private Boolean memory;
    /**
    * memory数值类型（value=数值，proportion=比例）
    */
    @ApiModelProperty(value = "memory数值类型（value=数值，proportion=比例）")
    private String memoryValueType;
    /**
    * memory数值
    */
    @ApiModelProperty(value = "memory数值")
    private Double memoryValue;
    /**
    * memory单位
    */
    @ApiModelProperty(value = "memory单位")
    private String memoryCompany;
}