package com.example.order_service.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class GatewayFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        String header = req.getHeader("X-Gateway-Auth");

        if (header == null) {
            ((HttpServletResponse) servletResponse).sendError(403, "Access only via API Gateway");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }
}
