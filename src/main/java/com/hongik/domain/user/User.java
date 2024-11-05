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

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StudySession> studySessions = new ArrayList<>();

    @Builder
    public User(final String username, final String password, final String nickname, final String department, final Role role, final String sub) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.department = department;
        this.role = role;
        this.sub = sub;
    }

    public void join(final String nickname, final String department) {
        this.nickname = nickname;
        this.department = department;
        this.role = Role.USER;
    }

    public void updateSub(final String sub) {
        this.sub = sub;
    }
}
