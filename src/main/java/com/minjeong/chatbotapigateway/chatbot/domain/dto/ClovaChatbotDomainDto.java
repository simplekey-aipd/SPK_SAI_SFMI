package com.minjeong.chatbotapigateway.chatbot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaChatbotDomainDto {

    private String chatbotName;
    private String domainId;
    private String signature;
    private String secretKey;
}
