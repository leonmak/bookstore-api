package com.example.bookstore.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
public class TracingHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        UUID uuid = UUID.randomUUID();
        request.setAttribute("correlation-id", uuid);
        return true; // If false, the request will be terminated
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        var uuid = request.getAttribute("correlation-id").toString();
        response.setHeader("X-Correlation-ID", uuid);
    }
}
