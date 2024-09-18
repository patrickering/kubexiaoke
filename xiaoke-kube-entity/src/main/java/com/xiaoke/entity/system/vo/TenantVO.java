package com.xiaoke.entity.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.system.entity.Tenant;
import com.xiaoke.entity.system.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 租户 VO
 *
 * @author 超级管理员
 * @date 2022-07-30 10:09:29
 */
@Data
@ApiModel(value = "租户VO")
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant")
public class TenantVO extends Tenant {

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity = User.class, field = "name", condition = "create_by=id")
    private String createName;
}