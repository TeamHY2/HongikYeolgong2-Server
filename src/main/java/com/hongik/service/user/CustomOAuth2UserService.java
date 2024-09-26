package com.hongik.service.user;

import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.user.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // TODO
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }

//        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String username = oAuth2User.getAttribute("email"); // email
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            User savedUser = userRepository.save(User.builder()
                    .username(oAuth2Response.getEmail())
                    .nickname(oAuth2Response.getName())
                    .role(Role.USER)
                    .build());

            return new CustomOAuth2User(savedUser);
        } else {
            user.updateProfile(oAuth2Response.getName());
            userRepository.save(user);

            return new CustomOAuth2User(user);
        }


    }
}
