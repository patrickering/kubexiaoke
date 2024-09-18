package com.xiaoke.entity.field;


import java.lang.annotation.*;

/**
 * @author xiaoke
 * @date 2021年5月24日17:02:12
 * <p>
 * UserFieldValue自定义注解（具体看readme.md文件）
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserFieldValue {

	/**
	 * 关联字段（查询条件）
	 *
	 * @return
	 */
	String fieldBy() default "";
}
