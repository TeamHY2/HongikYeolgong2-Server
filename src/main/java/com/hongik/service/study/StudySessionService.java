package com.hongik.service.study;

import com.hongik.domain.study.StudySession;
import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.response.StudySessionResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;

    private final UserRepository userRepository;

    @Transactional
    public StudySessionResponse createStudy(StudySessionCreateRequest request, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        StudySession savedStudySession = studySessionRepository.save(request.toEntity(user));

        return StudySessionResponse.of(savedStudySession);
    }
}
