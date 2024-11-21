package com.simplekey.spk_sai_sfmi.chatbot.domain.controller;


import com.simplekey.spk_sai_sfmi.chatbot.domain.dto.ChatbotDomainRequestDto;
import com.simplekey.spk_sai_sfmi.chatbot.domain.service.ChatbotDomainService;
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
    public ResponseEntity<?> addDomain(@RequestBody ChatbotDomainRequestDto dto) {
        return ResponseEntity.ok(chatbotDomainService.addDomain(dto));
    }

    @GetMapping("/getDomainInfo")
    public ResponseEntity<?> getDomainInfo(@RequestParam String domainId) {
        return ResponseEntity.ok(chatbotDomainService.getDomainInfo(domainId));
    }

}
