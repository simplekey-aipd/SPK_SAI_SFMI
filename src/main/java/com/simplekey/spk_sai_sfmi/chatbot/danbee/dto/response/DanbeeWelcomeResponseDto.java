package com.simplekey.spk_sai_sfmi.chatbot.danbee.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DanbeeWelcomeResponseDto {

    private String chatbotId;
    private String userId;
    // bp
    private String bpAction;    // chat, dtmf, end
    private String sessionId;
    private String nodeType;    // speak, slot
    private String message;


    public static DanbeeWelcomeResponseDto setDto(JsonNode resultNode, String bpAction) {
        return DanbeeWelcomeResponseDto.builder()
                .chatbotId(resultNode.get("chatbot_id").asText())
                .userId(resultNode.get("user_id").asText())
                .bpAction(bpAction)
                .sessionId(resultNode.get("session_id").asText())
                .nodeType(resultNode.get("result").get(0).get("nodeType").asText())
                .message(resultNode.get("result").get(0).get("message").asText())
                .build();
    }
}
