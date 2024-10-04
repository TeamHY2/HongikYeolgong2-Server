package com.hongik.domain.user;

import com.hongik.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public User(final String username, final String password, final String nickname, final String department, final Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.department = department;
        this.role = role;
    }

    public void join(final String nickname, final String department) {
        this.nickname = nickname;
        this.department = department;
        this.role = Role.USER;
    }
}
