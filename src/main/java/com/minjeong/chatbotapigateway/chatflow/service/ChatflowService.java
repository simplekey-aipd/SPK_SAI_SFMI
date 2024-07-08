package com.minjeong.chatbotapigateway.chatflow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.minjeong.chatbotapigateway.chatflow.dto.request.MessageRequestDto;
import com.minjeong.chatbotapigateway.chatflow.dto.request.WelcomeRequestDto;
import com.minjeong.chatbotapigateway.chatflow.dto.response.MessageResponseDto;
import com.minjeong.chatbotapigateway.chatflow.dto.response.WelcomeResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class ChatflowService {

    private static final String url = "https://danbee.ai/chatflow/chatbot/v1.0/bc81cab4-a8c6-458f-96c8-23855511bdd0";

    public WelcomeResponseDto getWelcomeMessage(WelcomeRequestDto dto) {
        String welcomeUrl = url + "/welcome.do";
        try {
            URL url = new URL(welcomeUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setDoOutput(true);

            // request body
            String jsonInputString = "{\"user_id\": \"" + dto.getUserId() + "\"}";

            // send request
            sendRequest(conn.getOutputStream(), jsonInputString);

            // get response
            int responseCode = conn.getResponseCode();
            log.info("Response Code: {}", responseCode);

            if (responseCode != 200) {
                log.error("!===== Error: {}", responseCode);
                return (WelcomeResponseDto) getErrorResponseMessage(conn.getErrorStream());
            }
            return getResponseMessage(conn.getInputStream());

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return null;
    }

    public MessageResponseDto sendMessage(MessageRequestDto dto) {
        String chatUrl = url + "/message.do";
        try {
            URL url = new URL(chatUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setDoOutput(true);

            // request body
            String jsonInputString = "";
            if (dto.getNodeType().equals("slot") || dto.getNodeType().equals("carousel")) {
                log.info("slot node");
                // input_sentence, user_id, session_id, ins_id, intent_id, node_id, param_id
                jsonInputString = "{\"input_sentence\": \"" + dto.getInputSentence() + "\", \"user_id\": \"" + dto.getUserId() + "\", \"session_id\": \"" + dto.getSessionId() + "\", \"ins_id\": \"" + dto.getInsId() + "\", \"intent_id\": \"" + dto.getIntentId() + "\", \"node_id\": \"" + dto.getNodeId() + "\", \"param_id\": \"" + dto.getParamId() + "\"}";
                log.info("input_sentence: {}, user_id: {}, session_id: {}, ins_id: {}, intent_id: {}, node_id: {}, param_id: {}", dto.getInputSentence(), dto.getUserId(), dto.getSessionId(), dto.getInsId(), dto.getIntentId(), dto.getNodeId(), dto.getParamId());
            } else {
                log.info("else type node");
                // input_sentence, user_id
                jsonInputString = "{\"input_sentence\": \"" + dto.getInputSentence() + "\", \"user_id\": \"" + dto.getUserId() + "\"}";
                log.info("input_sentence: {}, user_id: {}", dto.getInputSentence(), dto.getUserId());
            }

            // send request
            sendRequest(conn.getOutputStream(), jsonInputString);

            // get response
            int responseCode = conn.getResponseCode();
            log.info("Response Code: {}", responseCode);

            if (responseCode != 200) {
                log.error("!===== Error: {}", responseCode);
                return (MessageResponseDto) getErrorResponseMessage(conn.getErrorStream());
            }
            return makeMessageResponseDto(conn.getInputStream());
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return null;
    }

    public void sendRequest(OutputStream outputStream, String request) throws IOException {
        log.warn("### Request body: {}", request);
        DataOutputStream wr = new DataOutputStream(outputStream);
        wr.write(request.getBytes(StandardCharsets.UTF_8));
        wr.flush();
        wr.close();
    }

    public WelcomeResponseDto getResponseMessage(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readValue(inputStream, JsonNode.class);
            log.info("### getResponseMessage - JsonNode Response: {}", jsonNode);
            JsonNode resultNode = jsonNode.get("responseSet").get("result");
            String userId = resultNode.get("user_id").asText();
            String responseStr = resultNode.get("result").get(0).get("message").asText();
            String nodeType = resultNode.get("result").get(0).get("nodeType").asText();
            log.info("responseStr : {}", responseStr);
            log.info("nodeType : {}", nodeType);

            return WelcomeResponseDto.builder()
                    .userId(userId)
                    .bpAction("chat")
                    .nodeType(nodeType)
                    .message(responseStr)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getErrorResponseMessage(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String responseLine;
        try {
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }
        log.error("## Error Response: {}", response);
        return WelcomeResponseDto.builder()
                .bpAction("chat")
                .message(response.toString())
                .build();
    }

    private MessageResponseDto makeMessageResponseDto(InputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode node = objectMapper.readValue(inputStream, JsonNode.class);

        log.warn("### makeMessageResponseDto: {}", node.toString());
        JsonNode resultNode = node.get("responseSet").get("result");
        int msgResultNodeSize = resultNode.get("result").size();
        log.info("msgResultNode size: {}", msgResultNodeSize);

        // result.ins_id, intent_id, node_id, session_id, input_sentence, param_id
        // result.result.0.message, nodeType, result.resultStatus

        String bpAction = "chat";
        // resultNode.get("parameters").get("dtmf") 가 있는지 확인 후, 있다면 .asText() 값 = dtmf 일 경우 bpAction = "dtmf"
        if (resultNode.get("parameters").get("dtmf") != null) {
            if (resultNode.get("parameters").get("dtmf").asText().equals("dtmf")) {
                bpAction = "dtmf";
            }
        }

        // resultNode.get("result") 가 2개 이상일 경우
        // resultNode.get("result").get(1).get("nodeType").asText() 가 slot 인지 확인
        StringBuilder message = new StringBuilder();
        if (resultNode.get("result").isArray()) {
            ArrayNode arrayNode = (ArrayNode) resultNode.get("result");
            for (int i = 0; i < arrayNode.size(); i++) {
                log.info("### arrayNode: {}", arrayNode.get(i).get("message").asText());
                message.append(arrayNode.get(i).get("message").asText());
                message.append(" ");
            }
            log.info("result ArrayNode -> message: {}", message);
        }

        return MessageResponseDto.builder()
                .bpAction(bpAction)
                .insId(resultNode.get("ins_id").asText())
                .intentId(resultNode.get("intent_id").asText())
                .sessionId(resultNode.get("session_id").asText())
                .paramId(resultNode.get("param_id").asText())
                .nodeType(resultNode.get("result").get(msgResultNodeSize-1).get("nodeType").asText())
                .timestamp(resultNode.get("result").get(msgResultNodeSize-1).get("timestamp").asText())
                .message(message.toString())
                .inputSentence(resultNode.get("input_sentence").asText())
                .resultStatus(resultNode.get("resultStatus"))
                .build();
    }
}
