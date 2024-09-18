package com.xiaoke.entity.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 数据库表：sys_role
 *
 * @author Shaco
 * @create 2018-06-29
 */
@Data
@ApiModel(value = "角色")
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class Role extends Model<Role> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.AUTO)
    @ApiModelProperty(value = "角色编号")
    private Integer roleId;

    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @NotBlank(message = "角色标识不能为空")
    @ApiModelProperty(value = "角色标识")
    private String roleCode;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    @NotNull(message = "数据权限类型不能为空")
    @ApiModelProperty(value = "数据权限类型")
    private Integer dsType;
    /**
     * 数据权限作用范围
     */
    @ApiModelProperty(value = "数据权限作用范围")
    private String dsScope;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer createBy;

    /**
     * 删除标识（0-正常,1-删除）
     */
    @TableLogic
    @ApiModelProperty(value = "删除标记,1:已删除,0:正常")
    private String delFlag;
}