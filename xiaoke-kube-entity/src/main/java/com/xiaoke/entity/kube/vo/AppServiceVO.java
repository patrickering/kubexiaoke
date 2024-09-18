package com.xiaoke.entity.kube.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntityList;
import com.xiaoke.entity.kube.entity.AppPort;
import com.xiaoke.entity.kube.entity.AppService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 应用Service VO
 *
 * @author xiaoke
 * @date 2024-08-03 16:39:45
 */
@Data
@ApiModel(value = "应用Service")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_service")
public class AppServiceVO extends AppService {

    /**
     * 端口列表
     */
    @ApiModelProperty(value = "端口列表")
    @TableField(exist = false)
    @BindEntityList(entity = AppPort.class, condition = "id = service_id")
    private List<AppPort> portList;
}