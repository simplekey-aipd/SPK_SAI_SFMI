package com.simplekey.spk_sai_sfmi.chatbot.clova.controller;

import com.simplekey.spk_sai_sfmi.chatbot.clova.dto.ChatbotRequestDto;
import com.simplekey.spk_sai_sfmi.chatbot.clova.service.ClovaChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chatbot/clova/v1")
public class ClovaChatbotController {

    private final ClovaChatbotService clovaChatbotService;

    @PostMapping("/{chatbotId}/open")
    public ResponseEntity<?> openChatbot(@PathVariable String chatbotId, @RequestBody ChatbotRequestDto requestDto) {
        requestDto.setText("");
        return ResponseEntity.ok(clovaChatbotService.sendRequest(chatbotId, requestDto, "open"));
    }

    @PostMapping("/{chatbotId}/send")
    public ResponseEntity<?> sendChatbotMessage(@PathVariable String chatbotId, @RequestBody ChatbotRequestDto requestDto) {
        return ResponseEntity.ok(clovaChatbotService.sendRequest(chatbotId, requestDto, "send"));
    }

    @PostMapping("/timeout")
    public ResponseEntity<?> timeoutChatbot(@RequestBody ChatbotRequestDto requestDto) {
        return null;
    }

    @PostMapping("/{chatbotId}/close")
    public ResponseEntity<?> closeChatbot(@PathVariable String chatbotId, @RequestBody ChatbotRequestDto requestDto) {
        log.info("# Close Chatbot : " + chatbotId);
        return ResponseEntity.ok(clovaChatbotService.closeChatbot(chatbotId, requestDto));
    }

}
