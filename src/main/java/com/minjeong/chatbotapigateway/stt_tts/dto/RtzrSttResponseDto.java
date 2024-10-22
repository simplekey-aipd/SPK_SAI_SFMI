package com.minjeong.chatbotapigateway.stt_tts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.json.JSONObject;

@Getter
@Builder
@AllArgsConstructor
public class RtzrSttResponseDto {

    private JSONObject results;
    private JSONObject alternatives;
    private String transcript;
}
