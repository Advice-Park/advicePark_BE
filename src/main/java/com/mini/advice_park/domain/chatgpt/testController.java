package com.mini.advice_park.domain.chatgpt;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-gpt")
public class testController {

    private final ChatService chatService;
    private final ChatgptService chatgptService;

    //chat-gpt 와 간단한 채팅 서비스 소스
    @PostMapping("")
    public String test(@RequestBody String question) {
        return chatService.getChatResponse(question);
    }

}
