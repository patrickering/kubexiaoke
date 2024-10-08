

package com.xiaoke.entity.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author xiaoke
 * @since 2017-11-20
 */
@Data
@ApiModel(value = "日志")
public class SysLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "日志编号")
	private Long id;
	/**
	 * 日志类型
	 */
	@NotBlank(message = "日志类型不能为空")
	@ApiModelProperty(value = "日志类型")
	private String type;
	/**
	 * 日志标题
	 */
	@NotBlank(message = "日志标题不能为空")
	@ApiModelProperty(value = "日志标题")
	private String title;
	/**
	 * 创建者
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
	 * 操作IP地址
	 */
	@ApiModelProperty(value = "操作ip地址")
	private String remoteAddr;
	/**
	 * 用户代理
	 */
	@ApiModelProperty(value = "用户代理")
	private String userAgent;
	/**
	 * 请求URI
	 */
	@ApiModelProperty(value = "请求uri")
	private String requestUri;
	/**
	 * 操作方式
	 */
	@ApiModelProperty(value = "操作方式")
	private String method;
	/**
	 * 操作提交的数据
	 */
	@ApiModelProperty(value = "提交数据")
	private String params;
	/**
	 * 执行时间
	 */
	@ApiModelProperty(value = "方法执行时间")
	private Long time;
	/**
	 * 异常信息
	 */
	@ApiModelProperty(value = "异常信息")
	private String exception;

	/**
	 * 删除标记
	 */
	@TableLogic
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	private String delFlag;



}
