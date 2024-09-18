package com.xiaoke.entity.kube.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntity;
import com.diboot.core.binding.annotation.BindEntityList;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.kube.entity.AppDomain;
import com.xiaoke.entity.kube.entity.AppDomainRule;
import com.xiaoke.entity.kube.entity.Domain;
import com.xiaoke.entity.kube.entity.ConfigVersion;
import com.xiaoke.entity.system.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * 应用域名 VO
 *
 * @author xiaoke
 * @date 2024-08-19 00:39:43
 */
@Data
@ApiModel(value = "应用域名VO")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_domain")
public class AppDomainVO extends AppDomain {

    /**
     * 域名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "域名")
    @BindEntity(entity = Domain.class, condition = "domain_id=id")
    private Domain domain;

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity = User.class, field = "name", condition = "create_by=id")
    private String createName;

    /**
     * 应用域名规则
     */
    @ApiModelProperty(value = "应用域名规则")
    @TableField(exist = false)
    @BindEntityList(entity = AppDomainRule.class, condition = "id=app_domain_id",deepBind = true)
    private List<AppDomainRuleVO> appDomainRuleList;


    /**
     * 配置文件
     */
    @ApiModelProperty(value = "配置文件")
    @TableField(exist = false)
    @BindEntity(entity = ConfigVersion.class, condition = "version_id=id",deepBind = true)
    private ConfigVersionVO configVersion;
}