package com.hongik.discord;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Message {

    private String content;

    @Builder
    public Message(final String content) {
        this.content = content;
    }
}
