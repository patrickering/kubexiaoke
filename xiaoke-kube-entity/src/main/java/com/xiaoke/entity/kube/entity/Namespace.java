package com.xiaoke.entity.kube.entity;


import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

/**
*  命名空间 实体类
*
*  @author xiaoke
*  @date 2024-08-03 15:00:35
*/
@Data
@ApiModel(value = "命名空间")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_namespace")
public class Namespace extends Model<Namespace> {


    /**
    * 主键ID
    */
    @ApiModelProperty(value = "主键ID")
    @TableId
    private Integer id;
    /**
    * 项目id
    */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
    * 名称
    */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
    * 标识
    */
    @ApiModelProperty(value = "标识")
    private String sign;
    /**
    * cpu请求
    */
    @ApiModelProperty(value = "cpu请求")
    private Double cpuRequest;
    /**
    * cpu上限
    */
    @ApiModelProperty(value = "cpu上限")
    private Double cpuLimit;
    /**
    * 内存请求
    */
    @ApiModelProperty(value = "内存请求")
    private Integer memoryRequest;
    /**
    * 内存上限
    */
    @ApiModelProperty(value = "内存上限")
    private Integer memoryLimit;
    /**
    * 是否删除(0=false，1=true)
    */
    @ApiModelProperty(value = "是否删除(0=false，1=true)")
    @TableLogic
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