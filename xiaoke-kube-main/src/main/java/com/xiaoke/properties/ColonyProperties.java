package com.xiaoke.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 登录 配置
 *
 * @author Dragon
 */
@Component
@ConfigurationProperties(prefix = "colony")
@Data
public class ColonyProperties {
    /**
     * 集群地址
     */
    String url;
    /**
     * 授权标识
     */
    String authorization;
    /**
     * 日志收集地址
     */
    String logUrl;
    /**
     * 日志收集账号
     */
    String logUsername;
    /**
     * 日志收集密码
     */
    String logPassword;
    /**
     * 监控地址
     */
    String monitorUrl;
    /**
     * 监控授权
     */
    String monitorAuth;

}