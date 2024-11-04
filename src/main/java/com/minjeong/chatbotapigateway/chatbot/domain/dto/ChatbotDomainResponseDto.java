package com.minjeong.chatbotapigateway.chatbot.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.minjeong.chatbotapigateway.chatbot.domain.domain.ChatbotDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(builderMethodName = "chatbotDomainResponseDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatbotDomainResponseDto {

    private String result;
    private String id;
    private String chatbotName;
    private String domainUrl;
    private String signature;
    private String secretKey;
    private String apiKey;
    private String type;

    @Builder(builderMethodName = "addChatbotDomainResponseDtoBuilder")
    public ChatbotDomainResponseDto(String result, String id, String chatbotName, String domainUrl, String type) {
        this.result = result;
        this.id = id;
        this.chatbotName = chatbotName;
        this.domainUrl = domainUrl;
        this.type = type;
    }

    public static ChatbotDomainResponseDto setDto(String result, ChatbotDomain chatbotDomain, String domainUrl) {
        return ChatbotDomainResponseDto.chatbotDomainResponseDtoBuilder()
                .result(result)
                .id(chatbotDomain.getId().toString())
                .chatbotName(chatbotDomain.getChatbotName())
                .domainUrl(domainUrl)
                .signature(chatbotDomain.getSignature())
                .secretKey(chatbotDomain.getSecretKey())
                .apiKey(chatbotDomain.getApiKey())
                .type(chatbotDomain.getType())
                .build();
    }

    public static ChatbotDomainResponseDto setDtoFromAddDomainMethod(String result, ChatbotDomain chatbotDomain) {
        return ChatbotDomainResponseDto.addChatbotDomainResponseDtoBuilder()
                .result(result)
                .id(chatbotDomain.getId().toString())
                .chatbotName(chatbotDomain.getChatbotName())
                .domainUrl(chatbotDomain.getDomainId())
                .type(chatbotDomain.getType())
                .build();
    }
}
