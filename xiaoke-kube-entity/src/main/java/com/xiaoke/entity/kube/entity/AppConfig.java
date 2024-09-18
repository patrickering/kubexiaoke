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
*  应用配置 实体类
*
*  @author xiaoke
*  @date 2024-08-03 16:31:30
*/
@Data
@ApiModel(value = "应用配置")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_config")
public class AppConfig extends Model<AppConfig> {


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
    * 配置版本ID
    */
    @ApiModelProperty(value = "配置版本ID")
    private Integer versionId;
    /**
    * 挂载目录
    */
    @ApiModelProperty(value = "挂载目录")
    private String catalog;
    /**
    * 文件名
    */
    @ApiModelProperty(value = "文件名")
    private String fileName;
}