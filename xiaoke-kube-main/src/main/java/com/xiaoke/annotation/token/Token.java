package com.xiaoke.annotation.token;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Token {
    /**
     * 是否验证密等
     *
     * @return false, true
     */
    boolean value() default true;
}
