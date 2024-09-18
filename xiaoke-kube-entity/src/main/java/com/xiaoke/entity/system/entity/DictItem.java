
package com.xiaoke.entity.system.entity;

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
 * 字典项
 *
 * @author xiaoke
 * @date 2019/03/19
 */
@Data
@ApiModel(value = "字典项")
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_item")
public class DictItem extends Model<DictItem> {
    private static final long serialVersionUID = 1L;
    /**
     * 编号
     */
    @TableId
    @ApiModelProperty(value = "字典项id")
    private Integer id;
    /**
     * 所属字典类id
     */
    @ApiModelProperty(value = "所属字典类id")
    private Integer dictId;
    /**
     * 数据值
     */
    @ApiModelProperty(value = "数据值")
    private String value;
    /**
     * 标签名
     */
    @ApiModelProperty(value = "标签名")
    private String label;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 排序（升序）
     */
    @ApiModelProperty(value = "排序值，默认升序")
    private Integer sort;
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
