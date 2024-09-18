package com.xiaoke.entity.kube.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntityList;
import com.xiaoke.entity.kube.entity.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 应用 DTO
 *
 * @author xiaoke
 * @date 2024-08-03 15:34:01
 */
@Data
@ApiModel(value = "应用")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app")
public class AppDTO extends App {
    /**
     * 配置列表
     */
    @ApiModelProperty(value = "配置列表")
    @TableField(exist = false)
    private List<AppConfig> configList;

    /**
     * Service列表
     */
    @ApiModelProperty(value = "Service列表")
    @TableField(exist = false)
    private List<AppServiceDTO> serviceList;

    /**
     * host列表
     */
    @ApiModelProperty(value = "host列表")
    @TableField(exist = false)
    private List<AppHost> hostList;

    /**
     * 存储列表
     */
    @ApiModelProperty(value = "存储列表")
    @TableField(exist = false)
    private List<AppStorage> storageList;

    /**
     * 自定义环境变量列表
     */
    @ApiModelProperty(value = "自定义环境变量列表")
    @TableField(exist = false)
    private List<AppCustomEnv> customEnvList;
}