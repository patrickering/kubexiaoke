package com.xiaoke.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaoke
 * @date 2019-05-16
 * <p>
 * 任务调度类型类型
 */
@Getter
@AllArgsConstructor
public enum JobTypeEnum {
	/*
	* 计划任务
	* */
	JOB_PLAN_JOB_HANDLER("PlanJobHandler","计划任务，id=");
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 描述
	 */
	private String description;
}
