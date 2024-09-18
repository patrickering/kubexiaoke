package com.xiaoke.annotation.log;

import java.lang.annotation.*;

/**
 * @author xiaoke
 * @date 2019年6月11日 00:35:52
 * 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	/**
	 * 描述
	 *
	 * @return {String}
	 */
	String value();
}
