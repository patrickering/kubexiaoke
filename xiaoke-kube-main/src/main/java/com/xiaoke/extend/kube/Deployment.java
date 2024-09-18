package com.xiaoke.extend.kube;

import lombok.Data;

/**
 * 无状态应用
 */
@Data
public class Deployment {
	/**
	 * 应用名称
	 */
	private String name;

	/**
	 * 命名空间
	 */
	private String nameSpace;

	/**
	 * 标签
	 */
	private String labels;

	/**
	 * 创建时间
	 */
	private String createDate;

}
