package com.hongik.service.user;

import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.user.request.UserCreateRequest;
import com.hongik.dto.user.request.UserJoinRequest;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import jakarta.validation.Valid;
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
        checkNicknameDuplication(request.getNickname());
        checkUsernameDuplication(request.getUsername());

        User savedUser = userRepository.save(request.toEntity(bCryptPasswordEncoder.encode(request.getPassword())));
        return UserResponse.of(savedUser);
    }

    public void checkNicknameDuplication(final String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new AppException(ErrorCode.ALREADY_EXIST_NICKNAME, ErrorCode.ALREADY_EXIST_NICKNAME.getMessage());
        }
    }

    public void checkUsernameDuplication(final String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new AppException(ErrorCode.ALREADY_EXIST_USERNAME, ErrorCode.ALREADY_EXIST_USERNAME.getMessage());
        }
    }

    /**
     * 소셜 로그인 후, 닉네임과 학과를 입력하여 최종 회원가입을 한다.
     */
    @Transactional
    public UserResponse join(UserJoinRequest request, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        checkNicknameDuplication(request.getNickname());

        user.join(request.getNickname(), request.getDepartment());
        return UserResponse.of(user);
    }
}
