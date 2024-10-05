package com.hongik.service.auth;

import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.auth.request.LoginRequest;
import com.hongik.dto.auth.response.GoogleInfoResponse;
import com.hongik.dto.auth.response.TokenResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import com.hongik.jwt.JwtUtil;
import com.hongik.service.auth.apple.AppleSocialLoginService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.redirect-url}")
    private String googleRedirectUrl;

    private final UserRepository userRepository;

    private final GoogleLoginService googleLoginService;
    private final AppleSocialLoginService appleSocialLoginService;
    private final JwtUtil jwtUtil;

    public String getGoogleLoginView() {

        return "https://accounts.google.com/o/oauth2/v2/auth?" + "client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUrl
                + "&response_type=code"
                + "&scope=email%20profile%20openid"
                + "&access_type=offline";
    }

    public TokenResponse login(LoginRequest request) {
        User user = null;
        boolean isAlreadyExist = false;
        String socialPlatform = request.getSocialPlatform();
        if (socialPlatform.equals("google")) {
            GoogleInfoResponse googleInfoResponse = googleLoginService.login(request);

            if (userRepository.findByUsername(googleInfoResponse.getEmail()).isPresent()) {
                user = userRepository.findByUsername(googleInfoResponse.getEmail()).get();
                isAlreadyExist = true;
            } else {
                user = userRepository.save(User.builder()
                        .username(googleInfoResponse.getEmail())
                        .role(Role.GUEST)
                        .build());
            }
        } else if (socialPlatform.equals("apple")) {
            Claims appleInfoResponse = appleSocialLoginService.login(request);
            String email = appleInfoResponse.get("email", String.class);

            if (userRepository.findByUsername(email).isPresent()) {
                user = userRepository.findByUsername(email).get();
                isAlreadyExist = true;

            } else {
                user = userRepository.save(User.builder()
                        .username(email)
                        .role(Role.GUEST)
                        .build());
            }
        }

        return TokenResponse.of(jwtUtil.createAccessToken(user, 24 * 60 * 60 * 1000 * 30L), isAlreadyExist);
    }

    public void deleteUser(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        userRepository.delete(user);
    }
}
