package com.xiaoke.annotation.permission;

import cn.hutool.core.util.StrUtil;
import com.xiaoke.common.core.enums.ResultStatusEnum;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.exception.CustomException;
import com.xiaoke.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 环绕通知
 * 拦截权限标识方法 ，权限验证
 *
 * @author xiaoke
 */
@Aspect
@Component
@AllArgsConstructor
public class PermissionAop {


    @Around("@annotation(com.xiaoke.annotation.permission.Permission)")
    public Object beforeSwitchDS(JoinPoint point) throws Throwable {
        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        //获得访问的方法名
        String methodName = point.getSignature().getName();

        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
        Object returnVal = null;

        // 得到访问的方法对象
        Method method = className.getMethod(methodName, argClass);
        // 判断是否存在@Permission注解
        if (method.isAnnotationPresent(Permission.class)) {
            Permission annotation = method.getAnnotation(Permission.class);
            //获取参数列表
            //获取 request
            HttpServletRequest request = ((ServletRequestAttributes) Objects
                    .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            //参数列表中无HttpServletRequest抛出异常
            if (request == null) {
                throw new Exception(className + "." + methodName + "缺少HttpServletRequest参数");
            }
            //获取用户信息
            SysUser model = SecurityUtils.getUser();
            if (model == null) {
                throw new CustomException(ResultStatusEnum.NOT_LOGIN);
            }

            boolean flag = false;
            for (String e : model.getPermissions()) {
                String[] split = annotation.value().split(",");
                for (String permission : split) {
                    if (StrUtil.isNotBlank(e) && e.equals(permission)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
            if (!flag) {
                throw new CustomException(ResultStatusEnum.NOT_AUTH);
            }
            // 获取当前执行的方法
            ProceedingJoinPoint function = (ProceedingJoinPoint) point;
            //执行方法
            returnVal = function.proceed();
        }
        return returnVal;

    }
}
