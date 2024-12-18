package com.hongik.discord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MessageService {

    @Value("${discord.url}")
    private String webhookUrl;

    public boolean sendMsg(String msg){

        try {
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


            // response에 대한 처리
            if(response.getStatusCode().value() != HttpStatus.NO_CONTENT.value()){
                log.error("메시지 전송 이후 에러 발생");
                return false;
            }

        } catch (Exception e) {
            log.error("에러 발생 :: " + e);
            return false;
        }

        return true;
    }
}
