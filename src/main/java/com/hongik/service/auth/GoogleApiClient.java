package com.hongik.service.auth;

import com.hongik.dto.auth.response.GoogleInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "googleClient", url = "https://www.googleapis.com/userinfo/v2/me")
public interface GoogleApiClient {

    @GetMapping
    GoogleInfoResponse googleInfo(
            @RequestHeader("Authorization") String token
    );
}
