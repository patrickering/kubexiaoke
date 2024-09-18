package com.xiaoke.entity.kube.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntity;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.AppDomainRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用域名规则 VO
 *
 * @author xiaoke
 * @date 2024-08-19 01:09:45
 */
@Data
@ApiModel(value = "应用域名规则")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_domain_rule")
public class AppDomainRuleVO extends AppDomainRule {
    /**
     * 应用
     */
    @ApiModelProperty(value = "应用")
    @TableField(exist = false)
    @BindEntity(entity = App.class, condition = "app_id=id", deepBind = true)
    private App app;
}