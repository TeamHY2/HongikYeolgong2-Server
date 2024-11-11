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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
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

//        return TokenResponse.of(jwtUtil.createAccessToken(user, 24 * 60 * 60 * 1000 * 365L), isAlreadyExist);
        return TokenResponse.of(jwtUtil.createAccessToken(user, 10000 * 30L), isAlreadyExist);
    }

    @Transactional
    public TokenResponse appleLogin(AppleLoginRequest request) {
        User user = null;
        boolean isAlreadyExist = false;

        // 토큰이 정상적인지 확인한다. sub값 추출
        Claims appleInfoResponse = appleSocialLoginService.login(request);
        String sub = appleInfoResponse.get("sub", String.class);
        String email = request.getEmail();

//        log.info("email = {}", email);
        // 신규 회원 (request.getEmail()이 비어있을 때 신규 회원)
        // 신규 회원일 때, sub에는 email 값이 같이 들어있는 상태다.
        if (request.getEmail().isBlank()) {
            log.info("email is blank");
            // 신규 회원으로 가입한 후, 다시 로그인 하였을 때
            // sub값을 이용하여 User를 조회하고, 존재하지 않을 경우 계정 생성
            if (userRepository.findBySub(sub).isEmpty()) {
                log.info("subIsEmpty, 신규 회원");
                user = userRepository.save(User.builder()
                        .username(appleInfoResponse.get("email", String.class))
                        .sub(sub)
                        .role(Role.GUEST)
                        .build());
            } else {
                log.info("subIsNotEmpty, 기존 회원");
                // 존재하는 경우
                user = userRepository.findBySub(sub).get();
                if (user.getRole().equals(Role.USER)) {
                    isAlreadyExist = true;
                }
            }
        } else {
            // else 문을 탈 때는, UID값이 존재한다는 뜻이고
            // UID가 존재한다면 findByPassword 를 통해서 계정을 찾을 수 있다.
            log.info("UID값 존재 = {}", email);
            user = userRepository.findByPassword(email).get();
            user.updateSub(sub);
            if (user.getRole().equals(Role.USER)) {
                isAlreadyExist = true;
            }
        }

//        // email로 회원을 찾고, 존재하면 sub값 갱신하여 로그인한다.
//        if (userRepository.findByUsername(email).isPresent()) {
//            user = userRepository.findByUsername(email).get();
//            user.updateSub(sub);
//            if (user.getRole().equals(Role.USER)) {
//                isAlreadyExist = true;
//            }
//        } else {
//            // 존재하지 않으면 새로운 회원을 만든다.
//            user = userRepository.save(User.builder()
//                    .username(email)
//                    .sub(sub)
//                    .role(Role.GUEST)
//                    .build());
//        }

        return TokenResponse.of(jwtUtil.createAccessToken(user, 24 * 60 * 60 * 1000 * 365L), isAlreadyExist);
    }

    public UserResponse deleteUser(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        userRepository.delete(user);
        return UserResponse.of(user);
    }
}
