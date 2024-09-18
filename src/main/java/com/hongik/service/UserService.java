package com.hongik.service;

import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
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
    public void signUp() {
        User savedUser = userRepository.save(User.builder()
                .role(Role.USER)
                .username("test")
                .department("도시환경")
                .nickname("병일이")
                .password(bCryptPasswordEncoder.encode("123"))
                .build());

        userRepository.save(savedUser);
    }
}
