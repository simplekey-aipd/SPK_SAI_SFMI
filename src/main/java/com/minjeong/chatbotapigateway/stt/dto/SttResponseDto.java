package com.minjeong.chatbotapigateway.stt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.json.JSONObject;

@Getter
@Builder
@AllArgsConstructor
public class SttResponseDto {

    private JSONObject results;
    private JSONObject alternatives;
    private String transcript;
}
