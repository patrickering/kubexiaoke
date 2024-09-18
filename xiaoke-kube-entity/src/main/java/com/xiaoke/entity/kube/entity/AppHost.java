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
*  应用Host 实体类
*
*  @author xiaoke
*  @date 2024-08-03 17:10:09
*/
@Data
@ApiModel(value = "应用Host")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_host")
public class AppHost extends Model<AppHost> {


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
    * Ip
    */
    @ApiModelProperty(value = "Ip")
    private String ip;
    /**
    * 域名
    */
    @ApiModelProperty(value = "域名")
    private String domain;
}