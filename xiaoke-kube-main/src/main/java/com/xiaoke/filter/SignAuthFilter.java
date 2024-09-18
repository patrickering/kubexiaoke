package com.xiaoke.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.xiaoke.common.core.constant.CacheConstants;
import com.xiaoke.common.core.utils.JsonUtils;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.properties.SecurityProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * 防篡改、防重放攻击过滤器
 */
@Slf4j
@Component
@AllArgsConstructor
public class SignAuthFilter implements Filter {
    private final SecurityProperties securityProperties;
    private final RedisTemplate redisTemplate;

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("初始化 SignAuthFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        //是否启用
        if (!securityProperties.getEnable()) {
            filterChain.doFilter(httpRequest, response);
            return;
        }

        Set<String> uriSet = new HashSet<>(securityProperties.getIgnoreSignUri());
        String requestUri = httpRequest.getRequestURI();
        boolean isMatch = false;
        for (String uri : uriSet) {
            isMatch = requestUri.contains(uri);
            if (isMatch) {
                break;
            }
        }
        log.info("当前请求的URI是==>{},isMatch==>{}", requestUri, isMatch);
        if (isMatch) {
            filterChain.doFilter(httpRequest, response);
            return;
        }
        String sign = httpRequest.getHeader("Request-Sign");
        String key = httpRequest.getHeader("Request-Key");
        Long timestamp = Convert.toLong(httpRequest.getHeader("Request-Timestamp"));

        if (timestamp == null) {
            returnFail("时间戳不允许为空", response);
            return;
        }

        if (StrUtil.isEmpty(sign)) {
            returnFail("签名不允许为空", response);
            return;
        }

        //重放时间限制（单位分）
        Long difference = DateUtil.between(DateUtil.date(), DateUtil.date(timestamp), DateUnit.MINUTE);
        if (difference >= 1) {
            log.info("前端时间戳：{},服务端时间戳：{}", DateUtil.date(timestamp), DateUtil.date());
            returnFail("已过期的签名", response);
            return;
        }

        if (redisTemplate.hasKey(CacheConstants.REQUEST_SIGN + sign)) {
            returnFail("重复请求", response);
            return;
        }

        if (key == null) {
            returnFail("请求KEY不允许为空", response);
            return;
        }

        String systemSign = SecureUtil.md5(CacheConstants.REQUEST_SALT + key + timestamp).toUpperCase();
        if (!StrUtil.equals(sign, systemSign)) {
            returnFail("签名验证未通过", response);
            return;
        }

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(CacheConstants.REQUEST_SIGN + sign, key
                , 2, TimeUnit.MINUTES);
        filterChain.doFilter(httpRequest, response);

    }

    private void returnFail(String msg, ServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        String result = JsonUtils.objectToJson(R.failed(msg));
        out.println(result);
        out.flush();
        out.close();
    }

    @Override
    public void destroy() {
        log.info("销毁 SignAuthFilter");
    }
}