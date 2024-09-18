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
public @interface AggregateFieldValue {
	/**
	 * 查询类型
	 * @return
	 */
	AggregateTypeEnum type();

	/**
	 * 主表关联字段
	 *
	 * @return
	 */
	String fieldBy() default "";

	/**
	 * 查询类
	 *
	 * @return
	 */
	Class entity();

	/**
	 * 查询列
	 * @return
	 */
	String selectColumn() default "";

	/**
	 * 条件列
	 * @return
	 */
	String conditionColumn() default "";

	/**
	 * 追加条件
	 * @return
	 */
	String condition() default "";



}
