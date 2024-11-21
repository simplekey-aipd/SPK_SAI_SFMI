package com.simplekey.spk_sai_sfmi.chatbot.domain.service;

import com.simplekey.spk_sai_sfmi.chatbot.domain.domain.ChatbotDomain;
import com.simplekey.spk_sai_sfmi.chatbot.domain.dto.ChatbotDomainRequestDto;
import com.simplekey.spk_sai_sfmi.chatbot.domain.dto.ChatbotDomainResponseDto;
import com.simplekey.spk_sai_sfmi.chatbot.domain.repository.ChatbotDomainRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatbotDomainService {

    private final ChatbotDomainRepository domainRepository;

    @Transactional
    public ChatbotDomainResponseDto addDomain(ChatbotDomainRequestDto dto) {
        log.info("[DomainService] - Add chatbot domain : name = {}, domainId = {}, type = {}", dto.getChatbotName(), dto.getDomainId(), dto.getType());
        ChatbotDomain chatbotDomain = ChatbotDomain.builder()
                .chatbotName(dto.getChatbotName())
                .domainId(dto.getDomainId())
                .signature(dto.getSignature())
                .secretKey(dto.getSecretKey())
                .apiKey(dto.getApiKey())
                .type(dto.getType())
                .build();
        ChatbotDomain saved = domainRepository.save(chatbotDomain);
        log.info("[DomainService] - Success addDomain : id = {}, domainId = {}", saved.getId(), saved.getDomainId());

        return ChatbotDomainResponseDto.setDtoFromAddDomainMethod("success", saved);
    }

    public ChatbotDomainResponseDto getDomainInfo(String domainId) {
        ChatbotDomain domainInfo = domainRepository.findByDomainId(domainId);
        log.info("[DomainService] - Get domainInfo : id = {}, domainId = {}", domainInfo.getId(), domainInfo.getDomainId());

        if (domainInfo.getType().equals("clova")) {
            return ChatbotDomainResponseDto.setDto("success", domainInfo, getClovaDomainUrl(domainInfo.getDomainId(), domainInfo.getSignature()));
        } else if (domainInfo.getType().equals("danbee")) {
            return ChatbotDomainResponseDto.setDto("success", domainInfo, getDanbeeDomainUrl(domainInfo.getDomainId()));
        }
        return null;
    }

    private String getClovaDomainUrl(String domainId, String signature) {
        String ncloudDomain = "https://clovachatbot.ncloud.com/api/chatbot/messenger/v1";
        return ncloudDomain + "/" + domainId + "/" + signature + "/message";
    }

    private String getDanbeeDomainUrl(String domainId) {
        String danbeeDomain = "https://danbee.ai/chatflow/chatbot/v1.0";
        return danbeeDomain + "/" + domainId;
    }
}
