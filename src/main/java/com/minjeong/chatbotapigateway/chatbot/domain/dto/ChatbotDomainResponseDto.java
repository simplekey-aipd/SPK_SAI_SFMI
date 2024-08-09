package com.minjeong.chatbotapigateway.chatbot.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatbotDomainResponseDto {

    private String result;
    private String id;
    private String chatbotName;
    private String domainId;
    private String signature;
}
