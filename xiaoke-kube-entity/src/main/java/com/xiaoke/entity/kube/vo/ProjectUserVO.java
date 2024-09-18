package com.xiaoke.entity.kube.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindEntity;
import com.xiaoke.entity.kube.entity.ProjectUser;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.entity.system.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目人员 实体类
 *
 * @author xiaoke
 * @date 2024-08-27 00:01:32
 */
@Data
@ApiModel(value = "项目人员")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_project_user")
public class ProjectUserVO extends ProjectUser {
    /**
     * 人员
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "人员")
    @BindEntity(entity = User.class, condition = "user_id=id",deepBind = true)
    private UserVO user;
}