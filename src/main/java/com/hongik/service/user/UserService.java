package com.hongik.service.user;

import com.hongik.discord.MessageService;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.user.request.UserCreateRequest;
import com.hongik.dto.user.request.UserJoinRequest;
import com.hongik.dto.user.response.JoinResponse;
import com.hongik.dto.user.response.NicknameResponse;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import com.hongik.jwt.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final MessageService messageService;

    @Transactional
    public UserResponse signUp(UserCreateRequest request) {
        if (checkNicknameDuplication(request.getNickname()).isDuplicate()) {
            throw new AppException(ErrorCode.ALREADY_EXIST_NICKNAME, ErrorCode.ALREADY_EXIST_NICKNAME.getMessage());
        }
        checkUsernameDuplication(request.getUsername());

        User savedUser = userRepository.save(request.toEntity(bCryptPasswordEncoder.encode(request.getPassword())));
        return UserResponse.of(savedUser);
    }

    public NicknameResponse checkNicknameDuplication(final String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            return NicknameResponse.of(nickname, true);
        }

        return NicknameResponse.of(nickname, false);
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
    public JoinResponse join(UserJoinRequest request, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        if (checkNicknameDuplication(request.getNickname()).isDuplicate()) {
            throw new AppException(ErrorCode.ALREADY_EXIST_NICKNAME, ErrorCode.ALREADY_EXIST_NICKNAME.getMessage());
        }

        user.join(request.getNickname(), request.getDepartment());
        String accessToken = jwtUtil.createAccessToken(user, 24 * 60 * 60 * 1000 * 365L);
        messageService.sendMsg(request.getNickname() + "님 회원가입 완료");
        return JoinResponse.of(user, accessToken);
    }

    public UserResponse getUserInfo(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        return UserResponse.of(user);
    }
}
