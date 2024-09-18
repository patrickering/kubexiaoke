package com.xiaoke.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@WebFilter(urlPatterns = {"/"}, filterName = "responseHeadFilter")
public class ResponseHeadFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, IOException {
        //增加响应头缺失代码
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("X-Frame-Options", "SAMEORIGIN");
        res.addHeader("Content-Type", "application/json");
        res.addHeader("Referer-Policy", "origin");
        res.addHeader("Content-Security-Policy", "object-src 'self'");
        res.addHeader("X-Permitted-Cross-Domain-Policies", "master-only");
        res.addHeader("X-Content-Type-Options", "nosniff");
        res.addHeader("X-XSS-Protection", "1; mode=block");
        res.addHeader("X-Download-Options", "noopen");
        res.addHeader("Cache-control", "no-cache");
        //处理cookie问题
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String value = cookie.getValue();
                StringBuilder builder = new StringBuilder();
                builder.append(cookie.getName() + "=" + value + ";");
                builder.append("Secure;");//Cookie设置Secure标识
                builder.append("HttpOnly;");//Cookie设置HttpOnly
                res.addHeader("Set-Cookie", builder.toString());
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}