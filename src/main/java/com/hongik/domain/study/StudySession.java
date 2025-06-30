package com.hongik.domain.study;

import com.hongik.domain.BaseEntity;
import com.hongik.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class StudySession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean studyStatus;

    @Builder
    public StudySession(final User user, final LocalDateTime startTime, final LocalDateTime endTime, final boolean studyStatus) {
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studyStatus = studyStatus;
    }

    public void updateStudy(final LocalDateTime endTime, final boolean studyStatus) {
        this.endTime = endTime;
        this.studyStatus = studyStatus;
    }
}
