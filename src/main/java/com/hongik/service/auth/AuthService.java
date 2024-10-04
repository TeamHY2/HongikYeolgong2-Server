package com.hongik.service.auth;

import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.auth.request.LoginRequest;
import com.hongik.dto.auth.response.GoogleInfoResponse;
import com.hongik.dto.auth.response.TokenResponse;
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

    public String getGoogleLoginView() {

        return "https://accounts.google.com/o/oauth2/v2/auth?" + "client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUrl
                + "&response_type=code"
                + "&scope=email%20profile%20openid"
                + "&access_type=offline";
    }

    public TokenResponse login(LoginRequest request) {
        String socialPlatform = request.getSocialPlatform();
        if (socialPlatform.equals("google")) {
            GoogleInfoResponse googleInfoResponse = googleLoginService.login(request);
            User user = userRepository.findByUsername(googleInfoResponse.getEmail()).get();
            System.out.println("user.getDepartment() = " + user.getDepartment());
            System.out.println("user.getId() = " + user.getId());
            System.out.println("user.getNickname() = " + user.getNickname());
            return TokenResponse.of(user.getUsername());
        }

        return null;
    }
}
