package com.hongik.discord;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/discord")
@RestController
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public void sendMessage(@RequestBody Message message) {
//        messageService.sendMsg(message.getContent());
    }
}
