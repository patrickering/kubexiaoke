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
*  应用Service 实体类
*
*  @author xiaoke
*  @date 2024-08-03 16:39:45
*/
@Data
@ApiModel(value = "应用Service")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_service")
public class AppService extends Model<AppService> {


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
    * 类型
    */
    @ApiModelProperty(value = "类型")
    private String type;
    /**
    * 名称
    */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
    * 标识
    */
    @ApiModelProperty(value = "标识")
    private String sign;
    /**
    * 访问类型
    */
    @ApiModelProperty(value = "访问类型")
    private String visitType;
}