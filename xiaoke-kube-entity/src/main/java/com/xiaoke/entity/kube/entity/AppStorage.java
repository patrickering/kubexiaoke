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
*  应用存储 实体类
*
*  @author xiaoke
*  @date 2024-08-03 17:06:23
*/
@Data
@ApiModel(value = "应用存储")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_storage")
public class AppStorage extends Model<AppStorage> {


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
    * 存储ID
    */
    @ApiModelProperty(value = "存储ID")
    private Integer storageId;
    /**
    * 名称
    */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
    * 容量
    */
    @ApiModelProperty(value = "容量")
    private Integer capacity;
    /**
    * 挂载路径
    */
    @ApiModelProperty(value = "挂载路径")
    private String path;
}