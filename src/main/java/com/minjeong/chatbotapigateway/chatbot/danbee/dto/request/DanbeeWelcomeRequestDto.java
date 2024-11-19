package com.minjeong.chatbotapigateway.chatbot.danbee.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DanbeeWelcomeRequestDto {

    private String chatbotId;
    private String userId;
    private Map<String, Object> parameters;
}
