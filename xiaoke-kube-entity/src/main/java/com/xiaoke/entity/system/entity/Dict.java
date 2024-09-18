
package com.xiaoke.entity.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 字典表
 *
 * @author xiaoke
 * @date 2019/03/19
 */
@Data
@ApiModel(value = "字典类型")
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
public class Dict extends Model<Dict> {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    @ApiModelProperty(value = "字典编号")
    private Integer id;
    /**
     * 类型
     */
    @ApiModelProperty(value = "字典类型")
    private String type;
    /**
     * 描述
     */
    @ApiModelProperty(value = "字典描述")
    private String description;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    /**
     * 是否是系统内置
     */
    @TableField(value = "`system`")
    @ApiModelProperty(value = "是否系统内置")
    private String system;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 删除标记
     */
    @TableLogic
    @ApiModelProperty(value = "删除标记,1:已删除,0:正常")
    private String delFlag;


}
