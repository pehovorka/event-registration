package com.cscu9yw.eventregistrationbackend.security;

import com.cscu9yw.eventregistrationbackend.view.View;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;



@RestControllerAdvice
class SecurityJsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {
// Use Admin view for authenticated users and User view for everybody else.
    @Override
    protected void beforeBodyWriteInternal(
            MappingJacksonValue bodyContainer,
            MediaType contentType,
            MethodParameter returnType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        if (SecurityContextHolder.getContext().getAuthentication().getClass().equals(UsernamePasswordAuthenticationToken.class)) {
            bodyContainer.setSerializationView(View.MAPPING.get("ADMIN"));
        } else {
            bodyContainer.setSerializationView(View.MAPPING.get("USER"));
        }
    }
}