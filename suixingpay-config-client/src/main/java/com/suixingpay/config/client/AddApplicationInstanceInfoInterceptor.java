package com.suixingpay.config.client;

import com.suixingpay.config.common.Constant;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * 增加实例信息拦截器
 */
public class AddApplicationInstanceInfoInterceptor implements ClientHttpRequestInterceptor, Constant {

    private final SxfConfigClientProperties properties;

    public AddApplicationInstanceInfoInterceptor(SxfConfigClientProperties properties) {
        this.properties = properties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        request.getHeaders().add(IP_ADDRESS_HEADER, properties.getIpAddress());
        request.getHeaders().add(MANAGEMENT_PORT_HEADER, properties.getManagementPort());
        request.getHeaders().add(MANAGEMENT_CONTEXT_PATH_HEADER, properties.getManagementContextPath());

        return execution.execute(request, body);
    }
}
