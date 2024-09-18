package com.xiaoke.entity.kube.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntity;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.kube.entity.Config;
import com.xiaoke.entity.kube.entity.ConfigVersion;
import com.xiaoke.entity.system.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 配置版本 VO
 *
 * @author xiaoke
 * @date 2024-08-03 16:25:16
 */
@Data
@ApiModel(value = "配置版本VO")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_config_version")
public class ConfigVersionVO extends ConfigVersion {

    @TableField(exist = false)
    @ApiModelProperty(value = "配置文件")
    @BindEntity(entity = Config.class, condition = "config_id=id")
    private Config config;

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity = User.class, field = "name", condition = "create_by=id")
    private String createName;
}