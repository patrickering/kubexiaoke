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
*  应用pod记录 实体类
*
*  @author xiaoke
*  @date 2024-08-14 23:23:56
*/
@Data
@ApiModel(value = "应用pod记录")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_pod_record")
public class AppPodRecord extends Model<AppPodRecord> {


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
    * pod名称
    */
    @ApiModelProperty(value = "pod名称")
    private String podName;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}