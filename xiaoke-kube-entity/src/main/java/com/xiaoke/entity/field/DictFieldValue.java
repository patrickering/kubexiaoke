package com.xiaoke.entity.field;


import java.lang.annotation.*;

/**
 * @author xiaoke
 * @date 2021年5月24日17:02:12
 * <p>
 * DictFieldValue自定义注解（具体看readme.md文件）
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictFieldValue {

	/**
	 * 主表关联字段
	 *
	 * @return
	 */
	String fieldBy() default "";

	/**
	 * 字典类型
	 *
	 * @return
	 */
	String dictType() default "";


	/**
	 * 获取的列
	 *
	 * @return
	 */
	String getColumn() default "";
}
