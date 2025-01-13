package com.hongik.discord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class MessageService {

    @Value("${discord.url}")
    private String webhookUrl;

    @TransactionalEventListener
    public void sendMsg(MessageEvent event) {
        String msg = event.getMessage();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; utf-8");
        HttpEntity<Message> messageEntity = new HttpEntity<>(new Message(msg), httpHeaders);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(
                webhookUrl,
                HttpMethod.POST,
                messageEntity,
                String.class
        );
    }

}
