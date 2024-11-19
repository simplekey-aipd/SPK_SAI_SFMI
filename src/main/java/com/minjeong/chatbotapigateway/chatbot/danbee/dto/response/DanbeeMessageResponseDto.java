package com.minjeong.chatbotapigateway.chatbot.danbee.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DanbeeMessageResponseDto {

    private String chatbotId;
    private String userId;
    private String bpAction;    // chat, dtmf, end
    private String inputSentence;
    private String sessionId;
    private String insId;   // 대화 흐름 인스턴스 아이디
    private String intentId;
    private String nodeId;
    private String paramId;
    private String message;
    private String nodeType;    // speak, slot
    private JsonNode resultStatus;    // 결과 상태 정보
    private String userAnswer;  // 사용자 입력값

    @Builder(builderMethodName = "messageResponseDtoBuilder")
    public DanbeeMessageResponseDto(String chatbotId, String userId, String bpAction, String inputSentence,
                                    String sessionId, String insId, String intentId, String nodeId, String paramId,
                                    String message, String nodeType, JsonNode resultStatus, String userAnswer) {
        this.chatbotId = chatbotId;
        this.userId = userId;
        this.bpAction = bpAction;
        this.inputSentence = inputSentence;
        this.sessionId = sessionId;
        this.insId = insId;
        this.intentId = intentId;
        this.nodeId = nodeId;
        this.paramId = paramId;
        this.message = message;
        this.nodeType = nodeType;
        this.resultStatus = resultStatus;
        this.userAnswer = userAnswer;
    }

    // bpAction, message
    @Builder(builderMethodName = "errorMessageResponseDtoBuilder")
    public DanbeeMessageResponseDto(String bpAction, String message) {
        this.bpAction = bpAction;
        this.message = message;
    }

    public static DanbeeMessageResponseDto setDto(JsonNode resultNode, String bpAction, StringBuilder message) {
        log.info("resultNode : " + resultNode);
        int resultNodeSize = resultNode.get("result").size();
        return DanbeeMessageResponseDto.messageResponseDtoBuilder()
                .chatbotId(resultNode.get("chatbot_id").asText())
                .userId(resultNode.get("user_id").asText())
                .bpAction(bpAction)
                .inputSentence(resultNode.get("input_sentence").asText())
                .sessionId(resultNode.get("session_id").asText())
                .insId(resultNode.get("ins_id").asText())
                .intentId(resultNode.get("intent_id").asText())
                .nodeId(resultNode.get("node_id").asText())
                .paramId(resultNode.get("param_id").asText())
                .message(message.toString())
                .nodeType(resultNode.get("result").get(resultNodeSize-1).get("nodeType").asText())
                .resultStatus(resultNode.get("result_status"))
                .userAnswer(resultNode.get("parameters").get("@message").asText())
                .build();
    }

    public static DanbeeMessageResponseDto setErrorDto(String bpAction, String message) {
        return DanbeeMessageResponseDto.errorMessageResponseDtoBuilder()
                .bpAction(bpAction)
                .message(message)
                .build();
    }
}
