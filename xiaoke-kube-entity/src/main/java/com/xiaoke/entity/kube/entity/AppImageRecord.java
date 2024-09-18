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
 *  应用镜像更新记录 实体类
 *
 *  @author xiaoke
 *  @date 2024-08-14 00:03:31
 */
@Data
@ApiModel(value = "应用镜像更新记录")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_image_record")
public class AppImageRecord extends Model<AppImageRecord> {


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
     * 镜像
     */
    @ApiModelProperty(value = "镜像")
    private String image;
    /**
     * 镜像版本
     */
    @ApiModelProperty(value = "镜像版本")
    private String imageVersion;
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
}