package com.xiaoke.common.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信公众号 配置信息
 *
 * @author xiaoke
 */
@Data
@ConfigurationProperties(prefix = "wx")
public class WxProperties {
    private String wxAppId;
    private String wxAppSecret;

    private String miniAppId;
    private String miniAppSecret;
}
