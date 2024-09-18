

package com.xiaoke.annotation.idempotent;

import java.lang.annotation.*;

/**
 * @author  xiaoke
 * @date 2019/4/13
 * <p>
 * 幂等注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

	/**
	 * 是否验证密等
	 *
	 * @return false, true
	 */
	boolean value() default true;
}
