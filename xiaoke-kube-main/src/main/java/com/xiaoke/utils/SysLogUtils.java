package com.xiaoke.utils;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.JsonUtils;
import com.xiaoke.entity.system.entity.SysLog;
import com.xiaoke.entity.system.entity.SysUser;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 系统日志工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SysLogUtils {


    public SysLog getSysLog(ProceedingJoinPoint point) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SysLog sysLog = new SysLog();
        SysUser user = SecurityUtils.getUser();
        if (user != null && user.getId() != null) {
            sysLog.setCreateBy(Objects.requireNonNull(user.getId()));
        }
        sysLog.setType(Constant.STATUS_NORMAL);
        sysLog.setRemoteAddr(ServletUtil.getClientIP(request));
        sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        sysLog.setMethod(request.getMethod());
        sysLog.setUserAgent(request.getHeader("user-agent"));
        Object[] args = point.getArgs();
        if (args != null) {
            List<Object> paramList = new ArrayList<>();
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                } else {
                    if (arg != null) {
                        Object print = null;
                        try {
                            print = JsonUtils.objectToJson(arg);
                        } catch (Exception e) {
                            print = arg;
                        }
                        paramList.add(print);
                    }
                }
            }
            sysLog.setParams(JsonUtils.listToJsonStr(paramList));
        }
        return sysLog;
    }

}
