

package com.xiaoke.tenant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * @author xiaoke
 * @date 2020/4/29
 * <p>
 * 租户信息拦截
 */
@Configuration
public class KubeTenantConfiguration {

    @Bean
    public ClientHttpRequestInterceptor iotTenantRequestInterceptor() {
        return new TenantRequestInterceptor();
    }

}
