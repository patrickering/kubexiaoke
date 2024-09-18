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
*  应用域名规则 实体类
*
*  @author xiaoke
*  @date 2024-08-19 01:09:45
*/
@Data
@ApiModel(value = "应用域名规则")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_domain_rule")
public class AppDomainRule extends Model<AppDomainRule> {


    /**
    * 主键ID
    */
    @ApiModelProperty(value = "主键ID")
    @TableId
    private Integer id;
    /**
    * 应用域名ID
    */
    @ApiModelProperty(value = "应用域名ID")
    private Integer appDomainId;
    /**
    * 规则
    */
    @ApiModelProperty(value = "规则")
    private String path;
    /**
    * 应用ID
    */
    @ApiModelProperty(value = "应用ID")
    private Integer appId;
    /**
    * 应用类型
    */
    @ApiModelProperty(value = "应用类型")
    private String appType;
    /**
    * 服务标识
    */
    @ApiModelProperty(value = "服务标识")
    private String serviceSign;
    /**
    * 端口
    */
    @ApiModelProperty(value = "端口")
    private Integer port;
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