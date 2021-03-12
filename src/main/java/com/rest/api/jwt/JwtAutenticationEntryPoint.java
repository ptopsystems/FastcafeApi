package com.rest.api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.api.result.CommonResult;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAutenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        if(InsufficientAuthenticationException.class == authException.getClass()){
            PrintWriter pw = response.getWriter();
            pw.println(objectMapper.writeValueAsString(CommonResult.Fail(401, "유효하지 않은 토큰입니다.")));
            pw.flush();
        }
    }
}
