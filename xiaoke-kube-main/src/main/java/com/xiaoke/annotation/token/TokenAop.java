package com.xiaoke.annotation.token;

import com.xiaoke.common.core.enums.ResultStatusEnum;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.exception.CustomException;
import com.xiaoke.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 验证登录
 *
 * @author xiaoke
 */
@Aspect
@Component
@Slf4j
public class TokenAop {
    @Pointcut("execution(* com.xiaoke.model.*.controller..*(..))")
    public void tokenPointcut() {}

    /**
     * 方法执行前
     *
     * @param point
     * @throws Throwable
     */
    @Before("tokenPointcut()")
    public void doBefore(JoinPoint point) throws Throwable {
        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
        Method method = className.getMethod(methodName, argClass);
        // 判断是否存在@Permission注解
        if (method.isAnnotationPresent(Token.class)) {
            Token annotation = method.getAnnotation(Token.class);
            if (annotation != null) {
                if (!annotation.value()) {
                    //无需验证token
                    return;
                }
            }
        }

        SysUser user = SecurityUtils.getUser();
        //没有权限访问
        if (user == null) {
            throw new CustomException(ResultStatusEnum.NOT_LOGIN);
        }

    }
}
