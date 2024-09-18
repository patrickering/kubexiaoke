package com.xiaoke.entity.kube.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntityList;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.kube.entity.Config;
import com.xiaoke.entity.kube.entity.ConfigVersion;
import com.xiaoke.entity.system.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * 配置 VO
 *
 * @author xiaoke
 * @date 2024-08-03 16:10:50
 */
@Data
@ApiModel(value = "配置VO")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_config")
public class ConfigVO extends Config {

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity = User.class, field = "name", condition = "create_by=id")
    private String createName;


    /**
     * 版本列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = ConfigVersion.class, condition = "id=config_id", orderBy = "id:DESC")
    private List<ConfigVersion> versionList;
}