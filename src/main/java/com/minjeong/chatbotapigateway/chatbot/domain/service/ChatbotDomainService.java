package com.minjeong.chatbotapigateway.chatbot.domain.service;

import com.minjeong.chatbotapigateway.chatbot.domain.domain.ChatbotDomain;
import com.minjeong.chatbotapigateway.chatbot.domain.dto.ChatbotDomainDto;
import com.minjeong.chatbotapigateway.chatbot.domain.dto.ChatbotDomainResponseDto;
import com.minjeong.chatbotapigateway.chatbot.domain.repository.ChatbotDomainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatbotDomainService {

    private final ChatbotDomainRepository domainRepository;

    public ChatbotDomainResponseDto addDomain(ChatbotDomainDto dto) {
        ChatbotDomain chatbotDomain = ChatbotDomain.builder()
                .chatbotName(dto.getChatbotName())
                .domainId(dto.getDomainId())
                .signature(dto.getSignature())
                .secretKey(dto.getSecretKey())
                .build();
        ChatbotDomain saved = domainRepository.save(chatbotDomain);
        log.info("[DomainService] - Success addDomain : id = {}, domainId = {}",saved.getId(), saved.getDomainId());
        return ChatbotDomainResponseDto.builder()
                .result("success")
                .id(saved.getId().toString())
                .domainId(saved.getDomainId())
                .build();
    }

    public ChatbotDomainResponseDto getDomainInfo(String domainId) {
        ChatbotDomain domainInfo = domainRepository.findByDomainId(domainId);
        log.info("[DomainService] - Get domainInfo : id = {}, domainId = {}", domainInfo.getId(), domainInfo.getDomainId());
        return ChatbotDomainResponseDto.builder()
                .result("success")
                .id(domainInfo.getId().toString())
                .domainId(domainInfo.getDomainId())
                .chatbotName(domainInfo.getChatbotName())
                .signature(domainInfo.getSignature())
                .build();
    }
}
