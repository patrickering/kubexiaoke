package com.xiaoke.annotation.validate;

import com.xiaoke.common.core.utils.R;
import com.google.common.collect.Maps;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 验证注解处理类
 *
 * @author xiaoke
 */
@Component
@Aspect
public class ValidateAspectAop {

    private final String FLAG = "flag";

    /**
     * 使用AOP对使用了ValidateGroup的方法进行代理校验
     *
     * @throws Throwable
     */
    @Around("@annotation(com.xiaoke.annotation.validate.ValidateGroup)")
    public Object validateAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, Object> flagMap = Maps.newHashMap();
        ValidateGroup an = null;
        Object[] args = null;
        Method method = null;
        Object target = null;
        String methodName = null;
        try {
            methodName = joinPoint.getSignature().getName();
            target = joinPoint.getTarget();
            //得到拦截的方法
            method = getMethodByClassAndName(target.getClass(), methodName);
            //方法的参数
            args = joinPoint.getArgs();
            an = (ValidateGroup) getAnnotationByMethod(method, ValidateGroup.class);
            flagMap = validateFiled(an.fileds(), args);
        } catch (Exception e) {
            flagMap.put("flag", false);
            flagMap.put("msg", "验证参数异常！");
        }

        if (Boolean.valueOf(flagMap.get(FLAG).toString())) {
            return joinPoint.proceed();
        } else {
            return R.failed().setMsg(flagMap.get("msg").toString());
        }
    }

    /**
     * 验证参数是否合法
     */
    public Map<String, Object> validateFiled(ValidateFiled[] valiedatefiles, Object[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> returnMap = Maps.newHashMap();
        for (ValidateFiled validateFiled : valiedatefiles) {
            returnMap.put("flag", true);
            returnMap.put("msg", validateFiled.msg());
            Object arg = null;
            if ("".equals(validateFiled.filedName())) {
                arg = args[validateFiled.index()];
            } else {
                arg = getFieldByObjectAndFileName(args[validateFiled.index()],
                        validateFiled.filedName());
            }

            //判断参数是否为空
            if (validateFiled.notNull()) {
                if (arg == null) {
                    returnMap.put("flag", false);
                    return returnMap;
                }
            } else {//如果该参数能够为空，并且当参数为空时，就不用判断后面的了 ，直接返回true
                if (arg == null) {
                    returnMap.put("flag", true);
                    return returnMap;
                }
            }

            //判断字符串最大长度
            if (validateFiled.maxLen() > 0) {
                if (((String) arg).length() > validateFiled.maxLen()) {
                    returnMap.put("flag", false);
                    return returnMap;
                }
            }

            //判断字符串最小长度
            if (validateFiled.minLen() > 0) {
                if (((String) arg).length() < validateFiled.minLen()) {
                    returnMap.put("flag", false);
                    return returnMap;
                }
            }

            //判断数值最大值
            if (validateFiled.maxVal() != -1) {
                if ((Integer) arg > validateFiled.maxVal()) {
                    returnMap.put("flag", false);
                    return returnMap;
                }
            }

            //判断数值最小值
            if (validateFiled.minVal() != -1) {
                if ((Integer) arg < validateFiled.minVal()) {
                    returnMap.put("flag", false);
                    return returnMap;
                }
            }

            //判断正则
            if (!"".equals(validateFiled.regStr())) {
                if (arg instanceof String) {
                    if (!((String) arg).matches(validateFiled.regStr())) {
                        returnMap.put("flag", false);
                        return returnMap;
                    }
                } else {
                    returnMap.put("flag", false);
                    return returnMap;
                }
            }
        }
        return returnMap;
    }

    /**
     * 根据对象和属性名得到 属性
     */
    public Object getFieldByObjectAndFileName(Object targetObj, String fileName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String[] tmp = fileName.split("\\.");
        Object arg = targetObj;
        for (int i = 0; i < tmp.length; i++) {
            if (arg == null) {
                return arg;
            }
            Method methdo = arg.getClass().
                    getMethod(getGetterNameByFiledName(tmp[i]));
            arg = methdo.invoke(arg);
        }
        return arg;
    }

    /**
     * 根据属性名 得到该属性的getter方法名
     */
    public String getGetterNameByFiledName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * 根据目标方法和注解类型  得到该目标方法的指定注解
     */
    public Annotation getAnnotationByMethod(Method method, Class annoClass) {
        Annotation[] all = method.getAnnotations();
        for (Annotation annotation : all) {
            if (annotation.annotationType() == annoClass) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 根据类和方法名得到方法
     */
    public Method getMethodByClassAndName(Class c, String methodName) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
