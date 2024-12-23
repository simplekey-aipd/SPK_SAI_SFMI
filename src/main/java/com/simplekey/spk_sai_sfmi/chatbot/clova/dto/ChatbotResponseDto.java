package com.simplekey.spk_sai_sfmi.chatbot.clova.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponseDto {

    private String version;
    private String userId;
    private long timestamp;
    private List<Bubble> bubbles;
    private String description;
    private Map<String, String> variables;

    public static ChatbotResponseDto setDtoFromJson(JSONObject object, String event) {
        JSONArray bubbles = object.getJSONArray("bubbles");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bubbles.length(); i++) {
            sb.append(bubbles.getJSONObject(i).getJSONObject("data").getString("description"));
            sb.append(" ");
        }

        if (event.equals("open")) {
            return ChatbotResponseDto.builder()
                    .version(object.getString("version"))
                    .userId(object.getString("userId"))
                    .timestamp(object.getLong("timestamp"))
                    .bubbles(setBubbles(bubbles, event))
                    .description(sb.toString())
                    .variables(setVariableForOpen())
                    .build();
        }
        return ChatbotResponseDto.builder()
                .version(object.getString("version"))
                .userId(object.getString("userId"))
                .timestamp(object.getLong("timestamp"))
                .bubbles(setBubbles(bubbles, event))
                .description(sb.toString())
                .variables(setVariables(object, "user_answer"))
                .build();
    }

    private static List<Bubble> setBubbles(JSONArray bubbles, String event) {
        List<Bubble> bubbleList = new ArrayList<>();

        for (int i = 0; i < bubbles.length(); i++) {
            JSONObject bubble = bubbles.getJSONObject(i);
            Bubble bubbleObj = new Bubble();
            bubbleObj.setData(bubble.has("data") ? bubble.getJSONObject("data").toMap() : null);
            if (event.equals("open")) {
                bubbleObj.setContext(null);
                bubbleObj.setUserVariable(null);
            } else if (event.equals("send")) {
                bubbleObj.setContext(!bubble.isNull("context") && bubble.has("context") ? convertJsonArrayToList(bubble.getJSONArray("context")) : null);
                bubbleObj.setUserVariable(!bubble.isNull("userVariable") && bubble.has("userVariable") ? convertJsonArrayToList(bubble.getJSONArray("userVariable")) : null);
            }
            bubbleList.add(bubbleObj);
        }
        return bubbleList;
    }

    private static Map<String, String> setVariables(JSONObject response, String type) {
        Map<String, String> variables = new HashMap<>();
        JSONArray userVariables = response.getJSONArray("bubbles").getJSONObject(0).getJSONArray("userVariable");
        for (int i = 0; i < userVariables.length(); i++) {
            JSONObject userVariable = userVariables.getJSONObject(i);
            if (userVariable.getString("name").equals(type)) {
                variables.put(userVariable.getString("name"), userVariable.getString("value"));
            }
            if (userVariable.getString("name").equals("bpAction")) {
                variables.put(userVariable.getString("name"), userVariable.getString("value"));
            }
        }
        return variables;
    }

    // open 용 setVariable
    private static Map<String, String> setVariableForOpen() {
        Map<String, String> variables = new HashMap<>();
        variables.put("bpAction", "chat");
        return variables;
    }

    private static List<Map<String, Object>> convertJsonArrayToList(JSONArray jsonArray) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getJSONObject(i).toMap());
        }
        return list;
    }

    public static ChatbotResponseDto closeResponse(String chatbotId, String userId, long timestamp) {
        Map<String, String> variables = new HashMap<>();
        variables.put("chatbotId", chatbotId);
        variables.put("bpAction", "end");
        return new ChatbotResponseDto("v2", userId, timestamp, null, "close", variables);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Bubble {
        private Map<String, Object> data;
        private List<Map<String, Object>> context;
        private List<Map<String, Object>> userVariable;
    }
}
