package com.hongik.service.auth;

import com.hongik.dto.auth.request.GoogleIdTokenRequest;
import com.hongik.dto.auth.request.LoginRequest;
import com.hongik.dto.auth.response.GoogleInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleLoginService {

    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-url}")
    private String googleRedirectUrl;

    private final GoogleAuthApiClient googleAuthApiClient;
    private final GoogleApiClient googleApiClient;

    public GoogleInfoResponse login(LoginRequest request) {

//        System.out.println("request.getCode() = " + request.getCode());
//        System.out.println("googleClientId = " + googleClientId);
//        System.out.println("googleClientSecret = " + googleClientSecret);
//        System.out.println("googleRedirectUrl = " + googleRedirectUrl);
//        GoogleAuthResponse googleAuthResponse = googleAuthApiClient.googleAuth(
//                request.getId_token(),
//                googleClientId,
//                googleClientSecret,
//                googleRedirectUrl,
//                "authorization_code"
//        );
//        System.out.println("googleAuthResponse.getAccessToken() = " + googleAuthResponse.getAccess_token());
//        System.out.println("googleAuthResponse.getId_token() = " + googleAuthResponse.getId_token());

//        return null;
//        return googleApiClient.googleInfo("Bearer " + googleAuthResponse.getAccess_token());
        return googleApiClient.googleInfo(GoogleIdTokenRequest.builder()
                .id_token(request.getIdToken())
                .build());
    }
}
