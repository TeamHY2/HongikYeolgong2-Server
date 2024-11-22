package com.hongik.discord;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageService messageService;

    @DisplayName("Discord 메세지 전송 테스트")
    @Test
    void sendMsg() {
        // given
        BDDMockito.given(messageService.sendMsg(anyString()))
                .willReturn(true);

        // when
        boolean result = messageService.sendMsg("");

        // then
        assertThat(result).isTrue();
    }
}