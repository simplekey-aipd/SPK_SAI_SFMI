package com.simplekey.spk_sai_sfmi.chatbot.clova.service;

import com.simplekey.spk_sai_sfmi.api.sfmi.scm.domain.SfmiTargetRecord;
import com.simplekey.spk_sai_sfmi.chatbot.clova.dto.ChatbotRequestDto;
import com.simplekey.spk_sai_sfmi.chatbot.clova.dto.ChatbotResponseDto;
import com.simplekey.spk_sai_sfmi.chatbot.domain.dto.ChatbotDomainResponseDto;
import com.simplekey.spk_sai_sfmi.chatbot.domain.service.ChatbotDomainService;
import com.simplekey.spk_sai_sfmi.chatbot.global.error.BaseException;
import com.simplekey.spk_sai_sfmi.chatbot.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClovaChatbotService {

    private final ChatbotDomainService chatbotDomainService;

    public Object sendRequest(String chatbotId, ChatbotRequestDto requestDto, String event) {
        log.info("[sendRequest] Request message : {}, {}, {}", chatbotId, requestDto, event);
        ChatbotDomainResponseDto domainInfo = chatbotDomainService.getDomainInfo(chatbotId);
        String url = domainInfo.getDomainUrl();
        String secretKey = domainInfo.getSecretKey();
        return sendChatbotMessage(url, secretKey, requestDto, event);
    }

    public Object sendChatbotMessage(String apiUrl, String secretKey, ChatbotRequestDto requestDto, String event) {
        log.info("[sendChatbotMessage] Request message : {}, {}, {}, {}", apiUrl, requestDto.getUserId(), requestDto.getText(), event);
        String userId = requestDto.getUserId();
        String voiceMessage = requestDto.getText();

        Object resultMessage = null;

        try {
            URL url = new URL(apiUrl);
            log.info("setRequestMessage");
            String message = setRequestMessage(userId, voiceMessage, event);
            if (event.equals("send")) {
                log.info("[sendChatbotMessage] Request message \n userId : {}, message : {}", userId, voiceMessage);
            }

            String encodeBase64String = makeSignature(message, secretKey);

            // connection 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;utf-8");
            connection.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

            connection.setDoOutput(true);   // POST 요청의 데이터를 OutputStream 으로 넘겨주겠다는 옵션

            // DataOutputStream 으로 POST 데이터 전송
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.write(message.getBytes(StandardCharsets.UTF_8));
            wr.flush();
            wr.close();

            // response
            if (connection.getResponseCode() == 200) {
                resultMessage = getResponseMessage(connection.getInputStream(), event);
            } else {
                String errorResponse = getResponseErrorMessage(connection.getErrorStream());
                handleErrorResponse(getResponseErrorCode(errorResponse));
                log.error("Handle Error Response: {}", errorResponse);
            }
        } catch (BaseException e) {
            log.error("[sendChatbotMessage] Handled Error: {}", String.valueOf(e));
            throw e;
        } catch (Exception e) {
            log.error("[sendChatbotMessage] Unexpected Error: {}", String.valueOf(e));
            throw new BaseException(ErrorCode.UNKNOWN_SERVICE_ERROR);
        }
        log.info("[sendChatbotMessage] Response message : {}", resultMessage);
        return resultMessage;
    }

    private static String setRequestMessage(String userId, String voiceMessage, String event) {
        log.info("setRequestMessage - userId : {}, voiceMessage : {}, event : {}", userId, voiceMessage, event);
        JSONObject obj = new JSONObject();
        long timestamp = new Date().getTime();
        obj.put("version", "v2");
        obj.put("userId", userId);
        obj.put("timestamp", timestamp);

        JSONObject content_object = new JSONObject();
        content_object.put("type", "text");

        JSONObject data_object = new JSONObject();
        data_object.put("description", voiceMessage);
        content_object.put("data", data_object);

        JSONArray content_array = new JSONArray();
        content_array.put(content_object);

        obj.put("bubbles", content_array);
        obj.put("event", event);

        log.info("return obj.toString() : {}", obj);
        return obj.toString();
    }

    public String makeSignature(String message, String secretKey) {
        String encodeBase64String;

        try {
            byte[] secret_key_bytes = secretKey.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec signingKey = new SecretKeySpec(secret_key_bytes, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

        } catch (BaseException e) {
            log.error("[makeSignature] Handled Error: {}", String.valueOf(e));
            throw e;
        } catch (Exception e) {
            log.error("[makeSignature] Unexpected Error: {}", String.valueOf(e));
            throw new BaseException(ErrorCode.UNKNOWN_SERVICE_ERROR);
        }
        return encodeBase64String;
    }

    private static ChatbotResponseDto getResponseMessage(InputStream inputStream, String event) throws IOException {
        JSONObject responseMessageAsJson = getResponseMessageAsJson(inputStream);
        // responseMessageAsJson 에서 dtmf 값 가져오기
        log.info("[getResponseMessage] Chatbot Response Message : {}", responseMessageAsJson);
        return ChatbotResponseDto.setDtoFromJson(responseMessageAsJson, event);
    }

    private static String getResponseErrorMessage(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            responseBuilder.append(line);
        }
        br.close();
        return responseBuilder.toString();
    }

    private static void handleErrorResponse(int responseCode) {
        ErrorCode errorCode
                = switch (responseCode) {
            case 1000 -> ErrorCode.VERSION_NOT_SUPPORTED;
            case 1001 -> ErrorCode.NOT_FOUND_DOMAIN_CODE;
            case 1002 -> ErrorCode.CHECK_URL_PARAM_IS_INVALID;

            case 4000 -> ErrorCode.REQUEST_PARAM_INVALID;
            case 4010 -> ErrorCode.UNAUTHORIZED;
            case 4030 -> ErrorCode.FORBIDDEN_TO_ACCESS;
            case 4031 -> ErrorCode.SIGNATURE_VALIDATE_FAILED;
            case 4032 -> ErrorCode.TIMESTAMP_EXCEEDED_TIME_WINDOW;

            case 5000 -> ErrorCode.UNKNOWN_SERVICE_ERROR;
            case 5010 -> ErrorCode.CURRENT_PROTOCOL_VERSION_NOT_SUPPORT_THIS_REPLY_STRUCTURE;
            case 5020 -> ErrorCode.CALLS_TO_THIS_API_HAVE_EXCEEDED_THE_RATE_LIMIT;
            default -> throw new IllegalStateException("Unexpected value: " + responseCode);
        };
        throw new BaseException(errorCode);
    }

    private static JSONObject getResponseMessageAsJson(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            responseBuilder.append(line);
        }
        br.close();
        return new JSONObject(responseBuilder.toString());
    }

    private static int getResponseErrorCode(String responseMessage) {
        JSONObject responseMessageAsJson = new JSONObject(responseMessage);
        return responseMessageAsJson.getInt("code");
    }

    public Object closeChatbot(String chatbotId, ChatbotRequestDto requestDto) {
        return ChatbotResponseDto.closeResponse(chatbotId, requestDto.getUserId(), new Date().getTime());
    }

    public String getRequestBody(HttpServletRequest req) {
        String body;

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            req.setCharacterEncoding("UTF-8");
            InputStream inputStream = req.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        body = stringBuilder.toString();
        log.info("[ClovaChatbotService] getRequestBody : {}", body);
        return body;
    }

}
