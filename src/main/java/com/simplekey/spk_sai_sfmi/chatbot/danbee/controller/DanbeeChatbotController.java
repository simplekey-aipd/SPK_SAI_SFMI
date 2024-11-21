package com.simplekey.spk_sai_sfmi.chatbot.danbee.controller;

import com.simplekey.spk_sai_sfmi.chatbot.danbee.service.DanbeeChatbotService;
import com.simplekey.spk_sai_sfmi.chatbot.danbee.dto.request.DanbeeMessageRequestDto;
import com.simplekey.spk_sai_sfmi.chatbot.danbee.dto.request.DanbeeWelcomeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chatbot/danbee/v1")
public class DanbeeChatbotController {

    private final DanbeeChatbotService danbeeChatbotService;

    @PostMapping("/welcome")
    public ResponseEntity<?> welcome(@RequestBody DanbeeWelcomeRequestDto dto) {
        return ResponseEntity.ok(danbeeChatbotService.getWelcomeMessage(dto));
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody DanbeeMessageRequestDto dto) {
        return ResponseEntity.ok(danbeeChatbotService.sendMessage(dto));
    }

    /*
    TEST
    - /error
    - /msg
     */
    @GetMapping("/error")
    public ResponseEntity<?> error() {
        return ResponseEntity.ok(danbeeChatbotService.getErrorDto());
    }

    @GetMapping("/msg")
    public ResponseEntity<?> msg() {
        return ResponseEntity.ok(danbeeChatbotService.getMessageDto());
    }

    // TODO : close, timeout API 추가

}
