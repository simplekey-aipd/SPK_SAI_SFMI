package com.simplekey.spk_sai_sfmi.chatbot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotDomainRequestDto {

    private String chatbotName;
    private String domainId;
    private String signature;
    private String secretKey;
    private String apiKey;
    private String type;
}
