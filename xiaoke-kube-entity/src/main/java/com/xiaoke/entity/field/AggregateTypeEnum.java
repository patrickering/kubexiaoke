package com.xiaoke.entity.field;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaoke
 * @date 2019-05-16
 * <p>
 * 字典类型
 */
@Getter
@AllArgsConstructor
public enum AggregateTypeEnum {

	/**
	 * 数据条数
	 */
	COUNT("count", "数据条数"),
	/**
	 * 数据和
	 */
	SUM("sum", "数据和"),
	/**
	 * 数据平均值
	 */
	AVG("avg", "数据平均值");

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 描述
	 */
	private String description;

}
