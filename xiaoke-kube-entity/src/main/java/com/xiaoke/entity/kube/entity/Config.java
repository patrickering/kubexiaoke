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
*  配置 实体类
*
*  @author xiaoke
*  @date 2024-08-03 16:10:50
*/
@Data
@ApiModel(value = "配置")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_config")
public class Config extends Model<Config> {


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
    * 名称
    */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
    * 类型
    */
    @ApiModelProperty(value = "类型")
    private String type;
    /**
    * 最新版本
    */
    @ApiModelProperty(value = "最新版本")
    private Integer version;
    /**
    * 描述
    */
    @ApiModelProperty(value = "描述")
    private String remark;
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