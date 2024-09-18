package com.xiaoke.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件导出过滤器
 */
@Component
@Slf4j
@WebFilter(urlPatterns = {"/"}, filterName = "fileFilter")
public class FileFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getParameter("token");
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
        if (StrUtil.isNotEmpty(token)) {
            requestWrapper.addHeader("token", token);
            log.info("添加header-->{}", getHeadKeyAndValue(request));
        }
        filterChain.doFilter(requestWrapper, servletResponse);
    }

    private Map<String, String> getHeadKeyAndValue(HttpServletRequest httpRequest) {
        Map<String, String> header = new HashMap<>();
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String nextElement = headerNames.nextElement();
            header.put(nextElement, httpRequest.getHeader(nextElement));
        }
        return header;
    }
}