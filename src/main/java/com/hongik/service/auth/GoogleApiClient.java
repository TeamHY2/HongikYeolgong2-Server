package com.hongik.service.auth;

import com.hongik.dto.auth.request.GoogleIdTokenRequest;
import com.hongik.dto.auth.response.GoogleInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

//@FeignClient(value = "googleClient", url = "https://www.googleapis.com/userinfo/v2/me")
@FeignClient(value = "googleClient", url = "https://oauth2.googleapis.com/tokeninfo")
public interface GoogleApiClient {

    @PostMapping
    GoogleInfoResponse googleInfo(GoogleIdTokenRequest request);

//    @GetMapping
//    GoogleInfoResponse googleInfo(
//            @RequestHeader("Authorization") String token
//    );
}
