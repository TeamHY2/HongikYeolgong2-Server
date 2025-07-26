package com.hongik.domain.user;

import com.hongik.domain.BaseEntity;
import com.hongik.domain.study.StudySession;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // email

    private String password;

    private String nickname;

    private String department;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String sub;

    private String appleTransferSub;

    @Enumerated(EnumType.STRING)
    private SocialPlatform socialPlatform;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StudySession> studySessions = new ArrayList<>();

    private String deviceToken;

    @Builder
    public User(final String username, final String password, final String nickname, final String department, final Role role, final String sub, final SocialPlatform socialPlatform, final String deviceToken) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.department = department;
        this.role = role;
        this.sub = sub;
        this.socialPlatform = socialPlatform;
        this.deviceToken = deviceToken;
    }

    public void join(final String nickname, final String department) {
        this.nickname = nickname;
        this.department = department;
        this.role = Role.USER;
    }

    public void updateSub(final String sub) {
        this.sub = sub;
    }

    public void updateProfile(final String nickname, final String department) {
        this.nickname = nickname;
        this.department = department;
    }

    public void updateDeviceToken(final String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void updateAppleTransferSub(final String appleTransferSub) {
        this.appleTransferSub = appleTransferSub;
    }
}
