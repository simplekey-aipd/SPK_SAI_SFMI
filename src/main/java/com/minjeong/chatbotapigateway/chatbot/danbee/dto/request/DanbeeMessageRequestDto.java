package com.minjeong.chatbotapigateway.chatbot.danbee.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DanbeeMessageRequestDto {

    private String chatbotId;
    private String userId;
    private String inputSentence;
    private String sessionId;
    private String insId;
    private String intentId;
    private String nodeId;
    private String paramId;
    private String nodeType;
//    private JsonNode parameters;

    public DanbeeMessageRequestDto(String userId, String inputSentence, String sessionId, String insId, String intentId, String nodeId, String paramId, String nodeType) {
        this.userId = userId;
        this.inputSentence = inputSentence;
        this.sessionId = sessionId;
        this.insId = insId;
        this.intentId = intentId;
        this.nodeId = nodeId;
        this.paramId = paramId;
        this.nodeType = nodeType;
    }
}
