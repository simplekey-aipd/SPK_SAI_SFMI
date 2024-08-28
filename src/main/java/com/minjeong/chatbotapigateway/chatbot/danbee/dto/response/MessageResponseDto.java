package com.minjeong.chatbotapigateway.chatbot.danbee.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.*;

@Getter
@NoArgsConstructor
public class MessageResponseDto {

    // bp
    private String bpAction;    // chat, dtmf, end

    private String inputSentence;
    private String sessionId;
    private String insId;   // 대화 흐름 인스턴스 아이디
    private String intentId;
    private String paramId;
    private JsonNode result;   // 답변 메시지 결과 정보
    private String nodeType;
    private String timestamp;
    private String message;
    private JsonNode resultStatus;    // 결과 상태 정보
//    @Setter
//    private String responseMessage;

//    private String version;
//    private String chatbotId;
//    private String userId;
//    private Integer logId;  // message API
//    private String nodeId;
//    private String refIntentId;
//    private String chatflowId;
//    private ArrayNode anotherResult;    // NLU 에서 파악한 다른 의도 결과
//    private JsonNode parameters;
//    private String debugCode;
//    private String debugMsg;

    @Builder
    public MessageResponseDto(String bpAction, String insId, String intentId, String sessionId, String paramId
            , JsonNode result, String nodeType, String timestamp , String message, String inputSentence
            , JsonNode resultStatus) {
        this.bpAction = bpAction;
        this.insId = insId;
        this.intentId = intentId;
        this.sessionId = sessionId;
        this.paramId = paramId;
        this.result = result;
        this.nodeType = nodeType;
        this.timestamp = timestamp;
        this.message = message;
        this.inputSentence = inputSentence;
        this.resultStatus = resultStatus;
    }

}
