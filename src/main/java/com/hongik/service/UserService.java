package com.hongik.service;

import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.user.request.UserCreateRequest;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserResponse signUp(UserCreateRequest request) {
        User savedUser = userRepository.save(request.toEntity(bCryptPasswordEncoder.encode(request.getPassword())));
        return UserResponse.of(savedUser);
    }
}
