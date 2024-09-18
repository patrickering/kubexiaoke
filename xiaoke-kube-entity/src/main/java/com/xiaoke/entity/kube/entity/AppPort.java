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
*  应用端口 实体类
*
*  @author xiaoke
*  @date 2024-08-03 16:44:18
*/
@Data
@ApiModel(value = "应用端口")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_port")
public class AppPort extends Model<AppPort> {


    /**
    * 主键ID
    */
    @ApiModelProperty(value = "主键ID")
    @TableId
    private Integer id;
    /**
    * Service ID
    */
    @ApiModelProperty(value = "Service ID")
    private Integer serviceId;
    /**
    * 访问协议
    */
    @ApiModelProperty(value = "访问协议")
    private String agreement;
    /**
    * 容器端口
    */
    @ApiModelProperty(value = "容器端口")
    private Integer container;
    /**
    * 节点端口
    */
    @ApiModelProperty(value = "节点端口")
    private Integer node;
}