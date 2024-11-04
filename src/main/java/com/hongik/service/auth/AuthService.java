package com.hongik.service.auth;

import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.auth.request.AppleLoginRequest;
import com.hongik.dto.auth.request.LoginRequest;
import com.hongik.dto.auth.response.GoogleInfoResponse;
import com.hongik.dto.auth.response.TokenResponse;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import com.hongik.jwt.JwtUtil;
import com.hongik.service.auth.apple.AppleSocialLoginService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
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

    @Transactional
    public TokenResponse googleLogin(LoginRequest request) {
        User user = null;
        boolean isAlreadyExist = false;
        GoogleInfoResponse googleInfoResponse = googleLoginService.login(request);

        if (userRepository.findByUsername(googleInfoResponse.getEmail()).isPresent()) {
            // 이메일이 존재할 때 마이그레이션으로 인하여 sub값도 갱신해야 된다.
            user = userRepository.findByUsername(googleInfoResponse.getEmail()).get();
            user.updateSub(googleInfoResponse.getSub());
            if (user.getRole().equals(Role.USER)) {
                isAlreadyExist = true;
            }
        } else {
            user = userRepository.save(User.builder()
                    .username(googleInfoResponse.getEmail())
                    .sub(googleInfoResponse.getSub())
                    .role(Role.GUEST)
                    .build());
        }

        return TokenResponse.of(jwtUtil.createAccessToken(user, 24 * 60 * 60 * 1000 * 365L), isAlreadyExist);
    }

    @Transactional
    public TokenResponse appleLogin(AppleLoginRequest request) {
        User user = null;
        boolean isAlreadyExist = false;

        // 토큰이 정상적인지 확인한다. sub값 추출
        Claims appleInfoResponse = appleSocialLoginService.login(request);
        String sub = appleInfoResponse.get("sub", String.class);
        String email = request.getEmail();

        // email로 회원을 찾고, 존재하면 sub값 갱신하여 로그인한다.
        if (userRepository.findByUsername(email).isPresent()) {
            user = userRepository.findByUsername(email).get();
            user.updateSub(sub);
            if (user.getRole().equals(Role.USER)) {
                isAlreadyExist = true;
            }
        } else {
            // 존재하지 않으면 새로운 회원을 만든다.
            user = userRepository.save(User.builder()
                    .username(email)
                    .sub(sub)
                    .role(Role.GUEST)
                    .build());
        }

        return TokenResponse.of(jwtUtil.createAccessToken(user, 24 * 60 * 60 * 1000 * 365L), isAlreadyExist);
    }

    public UserResponse deleteUser(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        userRepository.delete(user);
        return UserResponse.of(user);
    }
}
