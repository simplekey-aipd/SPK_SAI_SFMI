package com.minjeong.chatbotapigateway.chatbot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DanbeeChatbotDomainDto {

    private String chatbotName;
    private String chatbotId;
    private String apiKey;
}
