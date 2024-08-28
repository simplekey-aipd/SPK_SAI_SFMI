package com.minjeong.chatbotapigateway.chatbot.domain.controller;


import com.minjeong.chatbotapigateway.chatbot.domain.dto.ChatbotDomainDto;
import com.minjeong.chatbotapigateway.chatbot.domain.dto.ChatbotDomainResponseDto;
import com.minjeong.chatbotapigateway.chatbot.domain.service.ChatbotDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chatbot/domain/v1")
public class ChatbotDomainController {

    private final ChatbotDomainService chatbotDomainService;

    @PostMapping("/add")
    public ResponseEntity<?> addDomain(@RequestBody ChatbotDomainDto dto) {
        ChatbotDomainResponseDto responseDto = chatbotDomainService.addDomain(dto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/getDomainInfo")
    public ResponseEntity<?> getDomainInfo(@RequestParam String domainId) {
        ChatbotDomainResponseDto domainInfo = chatbotDomainService.getDomainInfo(domainId);
        log.info("Get chatbot domain info : {}, {}", domainInfo.getChatbotName(), domainInfo.getDomainId());
        return ResponseEntity.ok(domainInfo);
    }

}
