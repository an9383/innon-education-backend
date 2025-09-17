package com.innon.education.jwt.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


    @Component
    public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

        private final Logger LOGGER = LoggerFactory.getLogger(getClass());

        private final HandlerExceptionResolver resolver;


        public RestAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver")HandlerExceptionResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public void commence( HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
                throws IOException {
            LOGGER.error("사용자가 승인되지 않았습니다. Entry Point(진입점)에서 라우팅 처리함. Message - {}", ex.getMessage());

            if (request.getAttribute("javax.servlet.error.exception") != null) {
                Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
                resolver.resolveException(request, response, null, (Exception) throwable);
            }

            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            }
        }

    }

