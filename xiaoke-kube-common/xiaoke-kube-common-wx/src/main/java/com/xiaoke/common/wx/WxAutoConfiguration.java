

package com.xiaoke.common.wx;

import com.xiaoke.common.wx.service.WxService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 微信 自动配置类
 *
 * @author xiaoke
 * @author 858695266
 */
@AllArgsConstructor
@EnableConfigurationProperties({WxProperties.class})
public class WxAutoConfiguration {

    private final WxProperties properties;
    private final RedisTemplate redisTemplate;

    @Bean
    @ConditionalOnMissingBean(WxService.class)
    public WxService wxService() {
        return new WxService(properties, redisTemplate);
    }

}
