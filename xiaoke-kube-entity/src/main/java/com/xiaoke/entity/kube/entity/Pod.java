package com.xiaoke.entity.kube.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
*  Pod 实体类
*
*  @author xiaoke
*  @date 2024-08-03 15:34:01
*/
@Data
@ApiModel(value = "Pod")
public class Pod {
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 节点名称
     */
    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    /**
     * cpu请求
     */
    @ApiModelProperty(value = "cpu请求")
    private BigDecimal cpuRequest;
    /**
     * cpu上限
     */
    @ApiModelProperty(value = "cpu上限")
    private BigDecimal cpuLimit;


    /**
     * 内存请求
     */
    @ApiModelProperty(value = "内存请求")
    private BigDecimal memoryRequest;
    /**
     * 内存上限
     */
    @ApiModelProperty(value = "内存上限")
    private BigDecimal memoryLimit;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String phase;
    /**
     * 重启次数
     */
    @ApiModelProperty(value = "重启次数")
    private Integer restartCount;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime createTime;



}