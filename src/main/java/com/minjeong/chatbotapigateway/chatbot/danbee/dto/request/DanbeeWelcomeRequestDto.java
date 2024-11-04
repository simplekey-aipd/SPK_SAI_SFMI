package com.minjeong.chatbotapigateway.chatbot.danbee.dto.request;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DanbeeWelcomeRequestDto {

    private String chatbotId;
    private String userId;
}
