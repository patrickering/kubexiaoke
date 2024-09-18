package com.xiaoke.entity.kube.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.diboot.core.binding.annotation.BindEntityList;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoke.entity.kube.entity.AppDomain;
import com.xiaoke.entity.kube.entity.AppDomainRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
*  应用域名 DTO
*
*  @author xiaoke
*  @date 2024-08-19 00:39:43
*/
@Data
@ApiModel(value = "应用域名")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_domain")
public class AppDomainDTO extends AppDomain {
    /**
     * 应用域名规则
     */
    @ApiModelProperty(value = "应用域名规则")
    @TableField(exist = false)
    private List<AppDomainRule> appDomainRuleList;
}