package com.minjeong.chatbotapigateway.chatbot.danbee.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class WelcomeResponseDto {

    private String userId;
    // bp
    private String bpAction;    // chat, dtmf, end
    private String nodeType;

    @Setter
    private String message;
}
