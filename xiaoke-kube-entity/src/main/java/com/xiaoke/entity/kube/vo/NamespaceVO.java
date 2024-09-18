package com.xiaoke.entity.kube.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.kube.entity.Namespace;
import com.xiaoke.entity.system.entity.User;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 命名空间 VO
 *
 * @author xiaoke
 * @date 2024-08-03 15:00:35
 */
@Data
@ApiModel(value = "命名空间VO")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_namespace")
public class NamespaceVO extends Namespace {

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity = User.class, field = "name", condition = "create_by=id")
    private String createName;

    /**
     * k8s 命名空间实例
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "k8s 命名空间实例")
    private V1Namespace nameSpace;

    /**
     * 当前cpu请求
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "当前cpu请求")
    private Double nowCpuRequest = 0.0;

    /**
     * 当前cpu上限
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "当前cpu上限")
    private Double nowCpuLimit = 0.0;

    /**
     * 当前内存请求
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "当前内存请求")
    private Double nowMemoryRequest = 0.0;

    /**
     * 当前内存上限
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "当前内存上限")
    private Double nowMemoryLimit = 0.0;

    /**
     * 运行中应用
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "运行中应用")
    private Integer runApp = 0;

    /**
     * 异常应用
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "异常应用")
    private Integer errorApp = 0;

    /**
     * 已停止应用
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "已停止应用")
    private Integer stopApp = 0;


    /**
     * 应用总数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "应用总数")
    private Integer appCount = 0;

    /**
     * 实例数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "实例数")
    private Integer exampleCount = 0;
}