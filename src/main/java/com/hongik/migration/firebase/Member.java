package com.hongik.migration.firebase;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class Member {

    private String id; // id값을 password에 넣고 활용하자.

    private String email;

    private String nickname;

    private String department;

    @Builder
    public Member(final String id, final String email, final String nickname, final String department) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.department = department;
    }
}
