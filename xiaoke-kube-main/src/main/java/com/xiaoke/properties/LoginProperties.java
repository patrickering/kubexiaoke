package com.xiaoke.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 登录 配置
 * @author Dragon
 */
@Component
@ConfigurationProperties(prefix = "login")
@Data
public class LoginProperties {
    /**
     * 是否启用登录密码错误限制
     */
    Boolean limitEnable;
    /**
     * 限制次数
     */
    Integer limitCount;
    /**
     * 超过次数后限制时间（小时）
     */
    Integer limitTime;
}