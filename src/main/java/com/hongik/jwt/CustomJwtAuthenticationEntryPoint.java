package com.hongik.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.exception.TokenErrorResponse;
import com.hongik.swagger.ErrorResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.io.IOException;

@Slf4j
@Component
public class CustomJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
        setResponse(request, response);
    }


    private void setResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message = request.getAttribute("message").toString();
        if (request.getRequestURI().equals("/error")) {
            message = "소셜 로그인 토큰 유효기간이 만료되었습니다. 재발급해주세요";
        }
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        TokenErrorResponse tokenErrorResponse = TokenErrorResponse.of(response.getStatus(), HttpStatus.UNAUTHORIZED.name(), message);

        response.getWriter()
                .write(objectMapper.writeValueAsString(tokenErrorResponse));
    }
}
