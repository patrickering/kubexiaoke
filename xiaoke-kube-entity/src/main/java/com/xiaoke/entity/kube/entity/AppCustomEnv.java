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
*  应用环境变量 实体类
*
*  @author xiaoke
*  @date 2024-08-03 15:45:33
*/
@Data
@ApiModel(value = "应用环境变量")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_custom_env")
public class AppCustomEnv extends Model<AppCustomEnv> {


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
    * 键
    */
    @ApiModelProperty(value = "键")
    private String envKey;
    /**
    * 值
    */
    @ApiModelProperty(value = "值")
    private String envValue;
}