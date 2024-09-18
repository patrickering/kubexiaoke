package com.xiaoke.entity.kube.vo;

import com.xiaoke.entity.kube.entity.AppImageRecord;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.system.entity.User;


/**
*  应用镜像更新记录 VO
*
*  @author xiaoke
*  @date 2024-08-14 00:03:31
*/
@Data
@ApiModel(value = "应用镜像更新记录VO")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_image_record")
public class AppImageRecordVO extends AppImageRecord {

    /**
    * 创建人姓名
    */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity= User.class, field="name", condition="create_by=id")
    private String createName;
}