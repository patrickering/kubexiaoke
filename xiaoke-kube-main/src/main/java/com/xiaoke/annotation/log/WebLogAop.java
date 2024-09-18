package com.xiaoke.annotation.log;

import com.xiaoke.common.sequence.sequence.Sequence;
import com.xiaoke.common.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 参数打印
 *
 * @author xiaoke
 */
@Aspect
@Component
@Slf4j
public class WebLogAop {
    @Autowired
    private Sequence sequence;
    private String serialNumber;

    @Pointcut("execution(* com.xiaoke.model.*.controller..*(..))")
    public void webLog() {
    }

    /**
     * 方法执行前
     *
     * @param point
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint point) {

        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attribute.getRequest();
        serialNumber = sequence.nextNo();
        log.info("流水号：[{}]=====================>接收请求 开始<=====================", serialNumber);
        log.info("流水号：[{}]请求URL：{}", serialNumber, request.getRequestURL().toString());
        log.info("流水号：[{}]请求类型：{}", serialNumber, request.getMethod());
        log.info("流水号：[{}]=====================>打印请求参数 开始<=====================", serialNumber);
        Object[] args = point.getArgs();
        log.info("流水号：[{}]参数数量：{}", serialNumber, args.length);
        Integer count = 0;
        for (Object arg : args) {
            count++;
            if (arg == null) {
                log.info("流水号：[{}][{}]值为 NULL", serialNumber, count);
            }else if(arg instanceof MultipartFile){
                log.info("流水号：[{}][{}]类型为文件", serialNumber, count);
            }  else {
                Object print = null;
                try {
                    print = JsonUtils.objectToJson(arg);
                } catch (Exception e) {
                    print = arg;
                }
                log.info("流水号：[{}][{}]{}：{}", serialNumber, count, arg.getClass().getSimpleName(), print);
            }
        }
        log.info("流水号：[{}]=====================>打印请求参数 结束<=====================", serialNumber);
        log.info("流水号：[{}]=====================>接收请求 结束<=====================", serialNumber);
    }

    /**
     * 方法执行结束
     *
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void aa(Object ret) throws Throwable {
        log.info("流水号：[{}]返回数据：{}", serialNumber, ret);
        log.info("流水号：[{}]=====================>请求处理 结束 流水号：{}<=====================", serialNumber, serialNumber);
    }

}
