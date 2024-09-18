package com.xiaoke.entity.kube.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntityList;
import com.diboot.core.binding.annotation.BindField;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.Storage;
import com.xiaoke.entity.system.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * 存储 VO
 *
 * @author xiaoke
 * @date 2024-08-03 17:00:02
 */
@Data
@ApiModel(value = "存储VO")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_storage")
public class StorageVO extends Storage {

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名")
    @BindField(entity = User.class, field = "name", condition = "create_by=id")
    private String createName;

    /**
     * 关联应用
     */
    @ApiModelProperty(value = "关联应用")
    @TableField(exist = false)
    @BindEntityList(entity = App.class, condition = "this.id=kube_app_storage.storage_id AND kube_app_storage.app_id=id")
    private List<App> relationAppList;
}