package com.hongik.dto.user.response;

import com.hongik.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final User user;

    public CustomOAuth2User(final User user) {
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_" + user.getRole().toString();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return user.getNickname();
    }

    public String getUsername() {
        return user.getUsername();
    }
}
