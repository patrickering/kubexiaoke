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
*  异常记录 实体类
*
*  @author xiaoke
*  @date 2024-08-22 23:06:02
*/
@Data
@ApiModel(value = "异常记录")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_error_record")
public class ErrorRecord extends Model<ErrorRecord> {


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
    * 标题
    */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
    * 已读
    */
    @ApiModelProperty(value = "已读")
    private Boolean readFlag;
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