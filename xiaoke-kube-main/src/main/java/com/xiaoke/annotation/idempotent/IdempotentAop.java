package com.xiaoke.annotation.idempotent;

import cn.hutool.core.util.StrUtil;
import com.xiaoke.common.core.constant.CacheConstants;
import com.xiaoke.common.core.enums.ResultStatusEnum;
import com.xiaoke.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 幂等
 *
 * @author xiaoke
 */
@Aspect
@Component
@Slf4j
public class IdempotentAop {
    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("execution(* com.xiaoke.model.*.controller..*(..))")
    public void idempotent() {
    }

    /**
     * 方法执行前
     *
     * @param point
     * @throws Throwable
     */
    @Before("idempotent()")
    public void doBefore(JoinPoint point) throws Throwable {
        String idempotent = this.checkIdempotent(point);
        if (StrUtil.isEmpty(idempotent)) {
            return;
        }
        String sign = CacheConstants.IDEMPOTENT_KEY + point.getSignature().getDeclaringTypeName() + StrUtil.DOT + point.getSignature().getName() + StrUtil.COLON + idempotent;
        Object flag = redisTemplate.opsForValue().get(sign);
        if (flag != null) {
            log.error("发生重复访问，key:{}", sign);
            throw new CustomException(ResultStatusEnum.REPEAT_VISIT);
        }
        log.info("缓存用户密等，key:{}", sign);
        //缓存用户幂等
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(sign, Boolean.TRUE, CacheConstants.CODE_TIME, TimeUnit.MINUTES);
    }

    /**
     * 是否需要验证幂等
     *
     * @param point
     * @return
     * @throws Exception
     */
    private String checkIdempotent(JoinPoint point) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        if (cn.hutool.http.Method.GET.name().equals(request.getMethod())) {
            //GET无须验证幂等继续执行
            return null;
        }

        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
        // 得到访问的方法对象
        Method method = className.getMethod(methodName, argClass);
        // 判断是否存在@Permission注解
        if (method.isAnnotationPresent(Idempotent.class)) {
            Idempotent annotation = method.getAnnotation(Idempotent.class);
            if (annotation != null) {
                if (!annotation.value()) {
                    //无须验证幂等继续执行
                    return null;
                }
            }
        }

        String idempotent = request.getHeader("idempotent");
        if (StrUtil.isEmpty(idempotent)) {
            throw new CustomException(ResultStatusEnum.NO_IDEMPOTENT_HEADER);
        }

        return idempotent;
    }


    /**
     * 方法执行结束
     *
     * @param object
     * @throws Throwable
     */
    @AfterReturning(returning = "object", pointcut = "idempotent()")
    public void doAfter(JoinPoint point, Object object) throws Exception {
        this.remove(point);
    }

    /**
     * 异常通知
     *
     * @param point
     */
    @AfterThrowing("execution(* com.xiaoke.model.*.controller..*(..))")
    public void doThrowing(JoinPoint point) throws Exception {
        this.remove(point);
    }

    /**
     * 移除幂等
     *
     * @param point
     */
    private void remove(JoinPoint point) throws Exception {
        String idempotent = this.checkIdempotent(point);
        if (StrUtil.isEmpty(idempotent)) {
            return;
        }
        String sign = CacheConstants.IDEMPOTENT_KEY + point.getSignature().getDeclaringTypeName() + StrUtil.DOT + point.getSignature().getName() + StrUtil.COLON + idempotent;
        //移除幂等
        log.info("移除用户密等，key:{}", sign);
        redisTemplate.delete(sign);
    }
}
