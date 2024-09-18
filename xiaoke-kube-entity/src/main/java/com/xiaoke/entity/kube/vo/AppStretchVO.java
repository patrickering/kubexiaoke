package com.xiaoke.entity.kube.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoke.entity.kube.entity.AppStretch;
import io.kubernetes.client.openapi.models.V2beta1HorizontalPodAutoscaler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 弹性伸缩 VO
 *
 * @author xiaoke
 * @date 2024-08-09 16:29:23
 */
@Data
@ApiModel(value = "弹性伸缩")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_stretch")
public class AppStretchVO extends AppStretch {
    @ApiModelProperty(value = "kubeJson")
    @TableField(exist = false)
    private V2beta1HorizontalPodAutoscaler kubeJson;

}