package com.xiaoke.entity.kube.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoke.entity.kube.entity.Config;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
*  配置 DTO
*
*  @author xiaoke
*  @date 2024-08-03 16:10:50
*/
@Data
@ApiModel(value = "配置")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_config")
public class ConfigDTO extends Config {


    /**
     * 版本内容
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "修改人")
    private String content;
}