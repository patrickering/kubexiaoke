package com.xiaoke.entity.kube.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntity;
import com.xiaoke.entity.kube.entity.AppStorage;
import com.xiaoke.entity.kube.entity.Storage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用存储 VO
 *
 * @author xiaoke
 * @date 2024-08-03 17:06:23
 */
@Data
@ApiModel(value = "应用存储")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app_storage")
public class AppStorageVO extends AppStorage {

    @ApiModelProperty(value = "存储")
    @TableField(exist = false)
    @BindEntity(entity = Storage.class, condition = "storage_id = id")
    private Storage storage;
}