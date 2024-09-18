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
*  应用域名 实体类
*
*  @author xiaoke
*  @date 2024-08-19 00:39:43
*/
@Data
@ApiModel(value = "应用域名")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_domain")
public class AppDomain extends Model<AppDomain> {


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
    * 协议
    */
    @ApiModelProperty(value = "协议")
    private String protocol;
    /**
    * 域名前缀
    */
    @ApiModelProperty(value = "域名前缀")
    private String domainPrefix;
    /**
    * 域名ID
    */
    @ApiModelProperty(value = "域名ID")
    private Integer domainId;
    /**
    * 配置文件版本ID
    */
    @ApiModelProperty(value = "配置文件版本ID")
    private Integer versionId;
    /**
    * 注解
    */
    @ApiModelProperty(value = "注解")
    private String annotations;
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