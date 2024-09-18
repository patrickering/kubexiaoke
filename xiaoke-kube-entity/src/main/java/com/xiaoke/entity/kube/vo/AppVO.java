package com.xiaoke.entity.kube.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntity;
import com.diboot.core.binding.annotation.BindEntityList;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.kube.entity.*;
import com.xiaoke.entity.system.entity.DictItem;
import com.xiaoke.entity.system.entity.User;
import io.kubernetes.client.openapi.models.V1Pod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * 应用 VO
 *
 * @author xiaoke
 * @date 2024-08-03 15:34:01
 */
@Data
@ApiModel(value = "应用VO")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app")
public class AppVO extends App {

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @BindEntity(entity = DictItem.class, condition = "state = value and type='app_state'")
    private DictItem stateDict;

    /**
     * 配置列表
     */
    @ApiModelProperty(value = "配置列表")
    @TableField(exist = false)
    @BindEntityList(entity = AppConfig.class, condition = "id = app_id", deepBind = true)
    private List<AppConfigVO> configList;

    /**
     * 存储列表
     */
    @ApiModelProperty(value = "存储列表")
    @TableField(exist = false)
    @BindEntityList(entity = AppStorage.class, condition = "id = app_id", deepBind = true)
    private List<AppStorageVO> storageList;

    /**
     * Service列表
     */
    @ApiModelProperty(value = "Service列表")
    @TableField(exist = false)
    @BindEntityList(entity = AppService.class, condition = "id = app_id", deepBind = true)
    private List<AppServiceVO> serviceList;

    /**
     * 自定义环境变量列表
     */
    @ApiModelProperty(value = "自定义环境变量列表")
    @TableField(exist = false)
    @BindEntityList(entity = AppCustomEnv.class, condition = "id = app_id")
    private List<AppCustomEnv> customEnvList;

    /**
     * host列表
     */
    @ApiModelProperty(value = "host列表")
    @TableField(exist = false)
    @BindEntityList(entity = AppHost.class, condition = "id = app_id")
    private List<AppHost> hostList;

    /**
     * pod
     */
    @ApiModelProperty(value = "pod")
    @TableField(exist = false)
    private List<V1Pod> pods;

    /**
     * 弹性伸缩列表
     */
    @ApiModelProperty(value = "弹性伸缩")
    @TableField(exist = false)
    @BindEntity(entity = AppStretch.class, condition = "id = app_id")
    private AppStretchVO appStretch;

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity = User.class, field = "name", condition = "create_by=id")
    private String createName;

    /**
     * 集群地址
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "集群地址")
    private String colonyUrl;

}