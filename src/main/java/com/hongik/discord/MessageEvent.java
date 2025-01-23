package com.hongik.discord;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageEvent extends ApplicationEvent {

    private final String message;

    public MessageEvent(final Object source, final String message) {
        super(source);
        this.message = message;
    }

}
