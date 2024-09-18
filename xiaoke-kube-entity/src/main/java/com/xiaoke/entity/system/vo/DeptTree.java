package com.xiaoke.entity.system.vo;

import com.xiaoke.entity.system.dto.TreeNode;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 部门 树实体类
 *
 * @author 超级管理员
 * @date 2022-07-16 11:42:46
 */
@Data
@ApiModel(value = "部门 树实体类")
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends TreeNode implements Serializable {


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 是否删除(0=false，1=true)
     */
    @ApiModelProperty(value = "是否删除(0=false，1=true)")
    private String delFlag;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Integer updateBy;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}