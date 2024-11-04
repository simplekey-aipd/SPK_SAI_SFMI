package com.minjeong.chatbotapigateway.chatbot.clova.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HcxToChatbotRequestDto {

    private String userId;
    private String text;
    private String category;
    private String resultCode;
    private String resultData;
}
