package com.xiaoke.entity.kube.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoke.entity.kube.entity.AppPort;
import com.xiaoke.entity.kube.entity.AppService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 应用Service DTO
 *
 * @author xiaoke
 * @date 2024-08-03 16:39:45
 */
@Data
@ApiModel(value = "应用Service")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_service")
public class AppServiceDTO extends AppService {
    /**
     * 端口列表
     */
    @ApiModelProperty(value = "端口列表")
    @TableField(exist = false)
    private List<AppPort> portList;

}