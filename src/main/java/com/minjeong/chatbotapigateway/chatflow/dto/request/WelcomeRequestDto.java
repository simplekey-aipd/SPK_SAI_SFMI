package com.minjeong.chatbotapigateway.chatflow.dto.request;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WelcomeRequestDto {

    private Integer sessionId;
    private String userId;
    private String postBackFlag;
    private ArrayNode parameters;

    public WelcomeRequestDto(String userId) {
        this.userId = userId;
    }
}
