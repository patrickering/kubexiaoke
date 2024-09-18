package com.xiaoke.entity.kube.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoke.entity.kube.entity.AppConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 应用配置 DTO
 *
 * @author xiaoke
 * @date 2024-08-03 16:31:30
 */
@Data
@ApiModel(value = "应用配置")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_config")
public class AppConfigDTO extends AppConfig {

    /**
     * id集合
     */
    @ApiModelProperty(value = "id集合")
    private List<Integer> idList;
}