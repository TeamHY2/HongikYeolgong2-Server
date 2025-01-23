package com.hongik.service.user;

import com.hongik.discord.MessageEvent;
import com.hongik.discord.MessageService;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.user.request.UserCreateRequest;
import com.hongik.dto.user.request.UserJoinRequest;
import com.hongik.dto.user.request.UserProfileRequest;
import com.hongik.dto.user.response.JoinResponse;
import com.hongik.dto.user.response.NicknameResponse;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import com.hongik.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final ApplicationEventPublisher eventPublisher;

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

        long userCount = userRepository.count();
        user.join(request.getNickname(), request.getDepartment());
        String accessToken = jwtUtil.createAccessToken(user, 24 * 60 * 60 * 1000 * 365L);
        eventPublisher.publishEvent(new MessageEvent(this, "[ " + userCount + "번째 유저 가입 ]\n" + "플랫폼: " + user.getSocialPlatform() + "\n" + "닉네임: " + request.getNickname()));
        return JoinResponse.of(user, accessToken);
    }

    public UserResponse getUserInfo(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse updateProfile(UserProfileRequest request, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        // 학과만 변경하는 경우는 닉네임 중복 검사를 하지 않는다.
        if (isNicknameChanged(request.getNickname(), user.getNickname())) {
            // 본인 닉네임과 변경하는 닉네임이 다른 경우는 닉네임 중복검사를 한다.
            if (checkNicknameDuplication(request.getNickname()).isDuplicate()) {
                throw new AppException(ErrorCode.ALREADY_EXIST_NICKNAME, ErrorCode.ALREADY_EXIST_NICKNAME.getMessage());
            }
        }

        user.updateProfile(request.getNickname(), request.getDepartment());

        return UserResponse.of(user);
    }

    /**
     * 닉네임 변경은 새로운 닉네임과 기존 닉네임이 다르기 때문에
     * 변경할 때는 True가 반환된다.
     */
    private boolean isNicknameChanged(final String newNickname, final String oldNickname) {
        return !newNickname.equals(oldNickname);
    }
}
