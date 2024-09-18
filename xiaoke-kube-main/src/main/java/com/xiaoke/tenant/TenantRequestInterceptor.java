package com.xiaoke.tenant;

import com.xiaoke.common.core.constant.Constant;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author xiaoke
 * @date 2020/4/29
 * <p>
 * 传递 RestTemplate 请求的租户ID
 */
public class TenantRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        if (TenantContextHolder.getTenantId() != null) {
            request.getHeaders().set(Constant.TENANT_ID, String.valueOf(TenantContextHolder.getTenantId()));
        }

        return execution.execute(request, body);
    }

}
