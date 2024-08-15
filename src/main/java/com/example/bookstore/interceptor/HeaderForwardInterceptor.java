package com.example.bookstore.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Component
public class HeaderForwardInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        var serverAttributes = (ServletRequestAttributes) attributes;
        var request = serverAttributes.getRequest();
        String uuid = request.getAttribute("correlation-id").toString();
        httpRequest.getHeaders().set("X-Correlation-Id", uuid);
        return execution.execute(httpRequest, body);
    }
}
