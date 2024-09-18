package com.xiaoke.entity.kube.vo;

import com.xiaoke.entity.kube.entity.Template;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.system.entity.User;


/**
*  模板 VO
*
*  @author xiaoke
*  @date 2024-08-17 21:23:43
*/
@Data
@ApiModel(value = "模板VO")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_template")
public class TemplateVO extends Template {

    /**
    * 创建人姓名
    */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity= User.class, field="name", condition="create_by=id")
    private String createName;
}