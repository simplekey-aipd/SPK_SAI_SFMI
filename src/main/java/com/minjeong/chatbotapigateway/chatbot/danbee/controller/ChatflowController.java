package com.minjeong.chatbotapigateway.chatbot.danbee.controller;

import com.minjeong.chatbotapigateway.chatbot.danbee.service.ChatflowService;
import com.minjeong.chatbotapigateway.chatbot.danbee.dto.request.MessageRequestDto;
import com.minjeong.chatbotapigateway.chatbot.danbee.dto.request.WelcomeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chatbot/danbee/v1")
public class ChatflowController {

    private final ChatflowService chatflowService;

    @PostMapping("/welcome")
    public ResponseEntity<?> welcome(@RequestBody WelcomeRequestDto dto) {
        return ResponseEntity.ok(chatflowService.getWelcomeMessage(dto));
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequestDto dto) {
        return ResponseEntity.ok(chatflowService.sendMessage(dto));
    }

    // TODO : close, timeout API 추가

}
