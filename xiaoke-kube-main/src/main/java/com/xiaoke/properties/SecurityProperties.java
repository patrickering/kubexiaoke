package com.xiaoke.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 签名验证 配置
 * @author Dragon
 */
@Component
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {
    /**
     * 是否启用
     */
    Boolean enable;
    /**
     * 允许忽略签名地址
     */
    List<String> ignoreSignUri;
}