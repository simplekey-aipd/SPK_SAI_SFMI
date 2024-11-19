package com.minjeong.chatbotapigateway.chatbot.danbee.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.minjeong.chatbotapigateway.chatbot.danbee.dto.request.DanbeeMessageRequestDto;
import com.minjeong.chatbotapigateway.chatbot.danbee.dto.request.DanbeeWelcomeRequestDto;
import com.minjeong.chatbotapigateway.chatbot.danbee.dto.response.DanbeeMessageResponseDto;
import com.minjeong.chatbotapigateway.chatbot.danbee.dto.response.DanbeeWelcomeResponseDto;
import com.minjeong.chatbotapigateway.chatbot.domain.dto.ChatbotDomainResponseDto;
import com.minjeong.chatbotapigateway.chatbot.domain.service.ChatbotDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class DanbeeChatbotService {

    private final ChatbotDomainService chatbotDomainService;

    public Object getWelcomeMessage(DanbeeWelcomeRequestDto dto) {
        ChatbotDomainResponseDto domainInfo = chatbotDomainService.getDomainInfo(dto.getChatbotId());
        String welcomeUrl = domainInfo.getDomainUrl() + "/welcome.do";

        try {
            URL url = new URL(welcomeUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setDoOutput(true);

            // request body
            // parameter 없을 경우
            if (dto.getParameters() == null) {
                String jsonInputString = "{\"user_id\": \"" + dto.getUserId() + "\"}";
                sendRequest(conn.getOutputStream(), jsonInputString);
            }

            JSONObject parameters = new JSONObject(dto.getParameters());
            String jsonInputString = "{\"user_id\": \"" + dto.getUserId() + "\", \"parameters\": " + parameters + "}";

            sendRequest(conn.getOutputStream(), jsonInputString);

            // get response
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                log.error("!===== Error: {}", responseCode);
                return getErrorResponseMessage(conn.getErrorStream());
            }

            return getResponseMessage(conn.getInputStream());
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return null;
    }

    public DanbeeMessageResponseDto sendMessage(DanbeeMessageRequestDto dto) {
        log.info("## sendMessage Message Request Dto : " + dto);

        ChatbotDomainResponseDto domainInfo = chatbotDomainService.getDomainInfo(dto.getChatbotId());
        String chatUrl = domainInfo.getDomainUrl() + "/message.do";

        try {
            URL url = new URL(chatUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setDoOutput(true);

            String nodeType = dto.getNodeType();
            // request body
            // slot node : user_id, input_sentence, session_id, ins_id, intent_id, node_id, param_id
            // speak node : user_id, input_sentence, session_id
            String jsonInputString = "";
            if (nodeType.equals("speak")) {
                jsonInputString = "{\"user_id\": \"" + dto.getUserId() + "\", \"input_sentence\": \""
                        + dto.getInputSentence() + "\", \"session_id\": \"" + dto.getSessionId() + "\"}";
            } else if (nodeType.equals("slot") || nodeType.equals("carousel")) {
                jsonInputString = "{\"user_id\": \"" + dto.getUserId() + "\", \"input_sentence\": \""
                        + dto.getInputSentence() + "\", \"session_id\": \"" + dto.getSessionId()
                        + "\", \"ins_id\": \"" + dto.getInsId() + "\", \"intent_id\": \"" + dto.getIntentId()
                        + "\", \"param_id\": \""  + dto.getParamId() + "\", \"node_id\": \"" + dto.getNodeId() + "\"}";
            } else {
                log.info("else type node");
                jsonInputString = "{\"input_sentence\": \"" + dto.getInputSentence() + "\", \"user_id\": \"" + dto.getUserId() + "\"}";
            }

            sendRequest(conn.getOutputStream(), jsonInputString);

            // get response
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                log.error("!===== Error: {}", responseCode);
                return (DanbeeMessageResponseDto) getErrorResponseMessage(conn.getErrorStream());
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

    public Object getResponseMessage(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readValue(inputStream, JsonNode.class);
            log.info("### getResponseMessage - JsonNode Response: {}", jsonNode);
            JsonNode resultNode = jsonNode.get("responseSet").get("result");

            int msgResultNodeSize = resultNode.get("result").size();
            log.info("msgResultNode size: {}", msgResultNodeSize);

            // welcome message 하나만 있을 경우 (speak node)
            if (msgResultNodeSize == 1) {
                log.info("### result.message: {}", resultNode.get("result").get(0).get("message").asText());
                return DanbeeWelcomeResponseDto.setDto(resultNode, "chat");
            }

            // 여러 개 message 있을 경우 (slot node)
            StringBuilder message = new StringBuilder();
            if (resultNode.get("result").isArray()) {
                ArrayNode arrayNode = (ArrayNode) resultNode.get("result");
                for (int i = 0; i < arrayNode.size(); i++) {
                    message.append(arrayNode.get(i).get("message").asText());
                    message.append(" ");
                }
                log.info("result.message -> message: {}", message);
            }
            return DanbeeMessageResponseDto.setDto(resultNode, "chat", message);
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
        return DanbeeMessageResponseDto.setErrorDto("error", response.toString());
    }

    private DanbeeMessageResponseDto makeMessageResponseDto(InputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            JsonNode node = objectMapper.readValue(inputStream, JsonNode.class);

            log.warn("### makeMessageResponseDto: {}", node.toString());
            JsonNode resultNode = node.get("responseSet").get("result");
            int msgResultNodeSize = resultNode.get("result").size();
            log.info("msgResultNode size: {}", msgResultNodeSize);

            String bpAction = "chat";
            if (resultNode.get("parameters").has("bpAction")) {
                bpAction = resultNode.get("parameters").get("bpAction").asText();
            }

            log.info("bpAction: {}", bpAction);

            StringBuilder message = new StringBuilder();
            if (resultNode.get("result").isArray()) {
                ArrayNode arrayNode = (ArrayNode) resultNode.get("result");
                for (int i = 0; i < arrayNode.size(); i++) {
                    message.append(arrayNode.get(i).get("message").asText());
                    message.append(" ");
                }
                log.info("result.message -> message: {}", message);
            }
            return DanbeeMessageResponseDto.setDto(resultNode, bpAction, message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    TEST
    - error
    - msg
     */
    public DanbeeMessageResponseDto getErrorDto() {
        return DanbeeMessageResponseDto.setErrorDto("error", "TEST - Error");
    }

    public DanbeeMessageResponseDto getMessageDto() {
        return DanbeeMessageResponseDto.messageResponseDtoBuilder()
                .chatbotId("test chatbotId")
                .userId("testt userId")
                .bpAction("chat")
                .inputSentence("inputSentence")
                .sessionId("sessionId")
                .insId("insId")
                .intentId("intentId")
                .nodeId("nodeId")
                .paramId("paramId")
                .message("message")
                .nodeType("nodeType")
//                .timestamp("timestamp")
                .resultStatus(null)
                .build();
    }
}
