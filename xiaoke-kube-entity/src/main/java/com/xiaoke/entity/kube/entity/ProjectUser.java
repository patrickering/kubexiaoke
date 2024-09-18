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
*  项目人员 实体类
*
*  @author xiaoke
*  @date 2024-08-27 00:01:32
*/
@Data
@ApiModel(value = "项目人员")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_project_user")
public class ProjectUser extends Model<ProjectUser> {


    /**
    * 主键ID
    */
    @ApiModelProperty(value = "主键ID")
    @TableId
    private Integer id;
    /**
    * 项目ID
    */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;
    /**
    * 用户ID
    */
    @ApiModelProperty(value = "用户ID")
    private Integer userId;
    /**
    * 是否关注
    */
    @ApiModelProperty(value = "是否关注")
    private Boolean followFlag;
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