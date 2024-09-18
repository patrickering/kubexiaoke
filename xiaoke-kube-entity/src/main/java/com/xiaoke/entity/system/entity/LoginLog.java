package com.xiaoke.entity.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
*  登录日志 实体类
*
*  @author xiaoke
*  @date 2022-01-23 15:30:21
*/
@Data
@ApiModel(value = "登录日志")
@EqualsAndHashCode(callSuper = true)
@TableName("sys_login_log")
public class LoginLog extends Model<LoginLog> {


    /**
    * 主键ID
    */
    @ApiModelProperty(value = "主键ID")
    @TableId
    private Integer id;
    /**
    * 类型
    */
    @ApiModelProperty(value = "类型")
    private String type;
    /**
    * IP
    */
    @ApiModelProperty(value = "IP")
    private String ip;
    /**
    * 来源
    */
    @ApiModelProperty(value = "来源")
    private String source;
    /**
    * Token
    */
    @ApiModelProperty(value = "Token")
    private String token;
    /**
    * 参数
    */
    @ApiModelProperty(value = "参数")
    private String param;
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