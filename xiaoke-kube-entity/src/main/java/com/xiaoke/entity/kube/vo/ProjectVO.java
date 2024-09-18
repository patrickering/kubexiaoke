package com.xiaoke.entity.kube.vo;

import com.xiaoke.entity.kube.entity.Project;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoke.entity.kube.entity.ProjectUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.system.entity.User;


/**
*  项目 VO
*
*  @author xiaoke
*  @date 2024-08-03 14:21:50
*/
@Data
@ApiModel(value = "项目VO")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_project")
public class ProjectVO extends Project {

    /**
    * 创建人姓名
    */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity= User.class, field="name", condition="create_by=id")
    private String createName;

    /**
     * 项目用户
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "项目用户")
    private ProjectUser projectUser;
}