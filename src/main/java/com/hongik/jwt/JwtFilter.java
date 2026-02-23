package com.hongik.jwt;

import com.hongik.exception.AppException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        log.info("Jwt Filter");
        log.info("request: {}", request.getRequestURI());
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("토큰값이 null입니다.");
            request.setAttribute("message", "토큰이 비어있습니다.(null)");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeader.split(" ")[1];
        if (jwtUtil.isExpired(accessToken)) {
            request.setAttribute("message", "토큰 유효기간이 만료되었습니다. 다시 로그인해주세요.");
            response.setStatus(401);
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(jwtUtil.getAuthentication(accessToken));
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }
}
