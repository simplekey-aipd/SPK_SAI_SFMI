package com.minjeong.chatbotapigateway.chatbot.clova.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponseDto {

    private String version;
    private String userId;
    private long timestamp;
    private String description;
    private String bpAction;    // chat, dtmf, end
    private String category;    // ADDR, DETAIL_ADDR, NAME, MOBILE, DATE, SCHEDULE, default 값 = ""

    public static ChatbotResponseDto fromJsonObject(JSONObject responseMessageAsJson, String event) {
        String bpAction = "";
        String category = "";

        if (event.equals("open")) {
            bpAction = "chat";
        } else if (event.equals("send")) {
            bpAction = setVariable(responseMessageAsJson, "bpAction");
            category = setVariable(responseMessageAsJson, "category");
        }

        log.info("[ChatbotResponseDto] - variables : bpAction = {}, category = {}", bpAction, category);

        // bubbles 의 data description 값이 여러 개 일 때 -> JSONArray 의 Object 개수 확인 후 description 값 이어붙이기

        JSONArray bubbles = responseMessageAsJson.getJSONArray("bubbles");
        if (bubbles.length() > 1) {
            StringBuilder description = new StringBuilder();
            for (int i = 0; i < bubbles.length(); i++) {
                description.append(bubbles.getJSONObject(i).getJSONObject("data").getString("description"));
                description.append(" ");
            }
            return new ChatbotResponseDto(
                    responseMessageAsJson.getString("version"),
                    responseMessageAsJson.getString("userId"),
                    responseMessageAsJson.getLong("timestamp"),
                    description.toString(),
                    bpAction,
                    category
            );
        }

        return new ChatbotResponseDto(
                responseMessageAsJson.getString("version"),
                responseMessageAsJson.getString("userId"),
                responseMessageAsJson.getLong("timestamp"),
                responseMessageAsJson.getJSONArray("bubbles").getJSONObject(0).getJSONObject("data").getString("description"),
                bpAction,
                category
        );
    }

    private static String setVariable(JSONObject response, String type) {
        JSONArray userVariables = response.getJSONArray("bubbles").getJSONObject(0).getJSONArray("userVariable");
        for (int i = 0; i < userVariables.length(); i++) {
            JSONObject userVariable = userVariables.getJSONObject(i);
            if (userVariable.getString("name").equals(type)) {
                return userVariable.getString("value");
            }
        }
        return "";
    }

    public static ChatbotResponseDto closeResponse(String chatbotId, String userId, long timestamp, String bpAction) {
        return new ChatbotResponseDto("v2", userId, timestamp, "chatbot id : " + chatbotId, bpAction, "");
    }
}
