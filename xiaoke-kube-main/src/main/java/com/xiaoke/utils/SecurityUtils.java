

package com.xiaoke.utils;


import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.model.system.service.SessionService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
@Slf4j
public class SecurityUtils {

    /**
     * 获取用户
     */
    public SysUser getUser() {
        //获取 bean
        SessionService<SysUser> sessionService = SpringUtil.getBean("sessionService");
        //获取 request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        if (request == null) {
            return null;
        }
        String token = request.getHeader("token");
        SysUser sysUser = sessionService.getSession(token);
        if (sysUser == null) {
            return null;
        }
        return sysUser;
    }

    /**
     * 设置用户
     */
    public String setUser(SysUser sysUser) {
        //获取 bean
        SessionService<SysUser> sessionService = SpringUtil.getBean("sessionService");
        String token = sessionService.setSession(sysUser);
        return token;
    }


    /**
     * 设置用户
     */
    public void removeUser() {
        //获取 bean
        SessionService<User> sessionService = SpringUtil.getBean("sessionService");
        //获取 request
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        User user = sessionService.getSession(request);
        if (user != null) {
            //移除用户
            sessionService.removeSession(request);
        }
    }

    public Boolean isAdmin() {
        String roleCode = SecurityUtils.getUser().getRole().getRoleCode();
        return StrUtil.equals(roleCode, "ROLE_ADMIN")||StrUtil.equals(roleCode, "ROLE_KUBE");
    }
}
