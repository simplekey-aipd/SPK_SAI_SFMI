package com.minjeong.chatbotapigateway.chatbot.clova.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRequestDto {

    private String chatbotId;
    private String userId;
    @Setter
    private String text;
    private String bpAction;
    private String timeoutCount;

}
