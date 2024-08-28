package com.minjeong.chatbotapigateway.chatbot.clova.controller;

import com.minjeong.chatbotapigateway.chatbot.clova.dto.ChatbotRequestDto;
import com.minjeong.chatbotapigateway.chatbot.clova.dto.HcxToChatbotRequestDto;
import com.minjeong.chatbotapigateway.chatbot.clova.service.ClovaChatbotService;
import com.minjeong.chatbotapigateway.chatbot.clova.service.HcxAndChatbotService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chatbot/clova/v1")
public class ClovaChatbotController {

    /*
    기존 : /chatbot/open or /chatbot/send -> domain(https://${domain}.apigw.ntruss.com/custom/v1/${domainId}/${signature}/message , body 에 secretKey 포함
    챗봇마다 다른 domainId, signature, secretKey
    -> jpa 로 db 에 저장하고, 챗봇마다 다른 설정을 할 수 있도록 하기
    ex) api : /chatbot/${chatbotId}/open or /chatbot/${chatbotId}/send -> db 에서 chatbotId 에 해당하는 domainId, signature, secretKey 가져와서 사용
     */

    private final ClovaChatbotService clovaChatbotService;
    private final HcxAndChatbotService hcxAndChatbotService;

    @PostMapping("/{chatbotId}/open")
    public ResponseEntity<?> openChatbot(@PathVariable String chatbotId, @RequestBody ChatbotRequestDto requestDto) {
        requestDto.setText("");
        return ResponseEntity.ok(clovaChatbotService.sendRequest(chatbotId, requestDto, "open"));
    }

    @PostMapping("/{chatbotId}/send")
    public ResponseEntity<?> sendChatbotMessage(@PathVariable String chatbotId, @RequestBody ChatbotRequestDto requestDto) {
        return ResponseEntity.ok(clovaChatbotService.sendRequest(chatbotId, requestDto, "send"));
    }

    @PostMapping("/{chatbotId}/sendHcxToChatbot")
    public ResponseEntity<?> sendHcxToChatbot(@PathVariable String chatbotId, @RequestBody HcxToChatbotRequestDto requestDto) {
        Object hcxToChatbotResponse = hcxAndChatbotService.sendHcxToChatbot(chatbotId, requestDto);
        log.info("HcxToChatbot Response : " + hcxToChatbotResponse);
        return ResponseEntity.ok(hcxToChatbotResponse);
    }

    // {chatbotId}/customApi/toHcx
    @PostMapping("/test/custom")
    public String testCoustom(HttpServletRequest req) {
        String customApiResponse = hcxAndChatbotService.customApi(req);
        log.info("Custom API Response : " + customApiResponse);
        return customApiResponse;
    }

    @PostMapping("/timeout")
    public ResponseEntity<?> timeoutChatbot(@RequestBody ChatbotRequestDto requestDto) {
        return null;
    }

    @PostMapping("/{chatbotId}/close")
    public ResponseEntity<?> closeChatbot(@PathVariable String chatbotId, @RequestBody ChatbotRequestDto requestDto) {
        log.info("Close Chatbot : " + chatbotId);
        return ResponseEntity.ok(clovaChatbotService.closeChatbot(chatbotId, requestDto));
    }

}
