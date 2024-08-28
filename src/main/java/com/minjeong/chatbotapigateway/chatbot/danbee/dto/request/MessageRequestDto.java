package com.minjeong.chatbotapigateway.chatbot.danbee.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequestDto {

    // required
    // bp
    private String nodeType;

    private String inputSentence;
    private String userId;
    private String sessionId;
    private String insId;
    private String intentId;
    private String paramId;

    private String nodeId;
    private String chatflowId;
    private String postBackFlag;
    private JsonNode parameters;

    public MessageRequestDto(String inputSentence, String userId, String nodeType) {
        this.inputSentence = inputSentence;
        this.userId = userId;
        this.nodeType = nodeType;
    }
}
