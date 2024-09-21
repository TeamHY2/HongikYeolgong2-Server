package com.hongik.service.user;

import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.user.response.CustomOAuth2User;
import com.hongik.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, SecurityException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String username = customOAuth2User.getUsername();
        User user = userRepository.findByUsername(username).get();

        String token = jwtUtil.createAccessToken(user, 24 * 60 * 60 * 1000 * 30L);
        response.addCookie(createCookie("Authorization", token));
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:8080");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60 * 1000 * 30);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
