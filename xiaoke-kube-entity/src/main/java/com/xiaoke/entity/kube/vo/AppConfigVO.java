package com.xiaoke.entity.kube.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntity;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.AppConfig;
import com.xiaoke.entity.kube.entity.Config;
import com.xiaoke.entity.kube.entity.ConfigVersion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用配置 VO
 *
 * @author xiaoke
 * @date 2024-08-03 16:31:30
 */
@Data
@ApiModel(value = "应用配置")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_config")
public class AppConfigVO extends AppConfig {
    /**
     * 配置版本
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "配置版本")
    @BindEntity(entity = ConfigVersion.class, condition = "version_id = id")
    private ConfigVersionVO version;

    /**
     * 配置文件
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "配置文件")
    @BindEntity(entity = Config.class, condition = "this.version_id=kube_config_version.id AND kube_config_version.config_id=id")
    private Config config;

    /**
     * 管理应用
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "管理应用")
    private App app;


}