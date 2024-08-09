package com.minjeong.chatbotapigateway.chatbot.clova.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponseDto {

    private String version;
    private String userId;
    private long timestamp;
    private String description;
    private String bpAction;    // chat, dtmf, end
    private int timeoutCount;

    public static ChatbotResponseDto fromJson(JSONObject responseMessageAsJson, String event, String scenario) {
        String bpAction = "";

        if (scenario.equals("default")) {
            if (event.equals("open")) {
                bpAction = "chat";
            } else if (event.equals("send")) {
                JSONArray userVariables = responseMessageAsJson.getJSONArray("bubbles").getJSONObject(0).getJSONArray("userVariable");
                for (int i = 0; i < userVariables.length(); i++) {
                    JSONObject userVariable = userVariables.getJSONObject(i);
                    if (userVariable.getString("name").equals("bpAction")) {
                        bpAction = userVariable.getString("value");
                        break;
                    }
                }
            }
        } else if (scenario.equals("dtmf")) {
            bpAction = "dtmf";
        }

        return new ChatbotResponseDto(
                responseMessageAsJson.getString("version"),
                responseMessageAsJson.getString("userId"),
                responseMessageAsJson.getLong("timestamp"),
                responseMessageAsJson.getJSONArray("bubbles").getJSONObject(0).getJSONObject("data").getString("description"),
                bpAction,
                0
        );
    }

    public static ChatbotResponseDto closeResponse(String userId, long timestamp, String bpAction) {
        return new ChatbotResponseDto("v2", userId, timestamp, null, bpAction, 0);
    }
}
