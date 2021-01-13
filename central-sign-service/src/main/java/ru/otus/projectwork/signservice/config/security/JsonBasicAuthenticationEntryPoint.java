package ru.otus.projectwork.signservice.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JsonBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-Type", "application/json");
        PrintWriter writer = response.getWriter();

        writer.print(String.format("{\"error\": \"%d\", \"message\": \"%s\"}", response.getStatus(), authException.getMessage()));
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("next_server");
        super.afterPropertiesSet();
    }
}