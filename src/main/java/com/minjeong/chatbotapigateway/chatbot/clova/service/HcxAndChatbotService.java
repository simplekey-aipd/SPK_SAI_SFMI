package com.minjeong.chatbotapigateway.chatbot.clova.service;

import com.minjeong.chatbotapigateway.chatbot.clova.dto.ChatbotRequestDto;
import com.minjeong.chatbotapigateway.chatbot.clova.dto.HcxToChatbotRequestDto;
import com.minjeong.chatbotapigateway.chatbot.domain.dto.ChatbotDomainResponseDto;
import com.minjeong.chatbotapigateway.chatbot.domain.service.ChatbotDomainService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HcxAndChatbotService {

    private final ChatbotDomainService chatbotDomainService;
    private final ClovaChatbotService clovaChatbotService;

    /*
     hcx 로 데이터 보내 1차 검증 또는 DB 저장 후
     챗봇으로 검증 결과, 응답 데이터 전송
    */

    public Object sendHcxToChatbot(String chatbotId, HcxToChatbotRequestDto requestDto) {
        ChatbotDomainResponseDto domainInfo = chatbotDomainService.getDomainInfo(chatbotId);
        String url = domainInfo.getDomainUrl();
        String secretKey = domainInfo.getSecretKey();

        log.info("[HcxToChatbotService] sendHcxToChatbot - result : {}", requestDto.getResultData());

        // HcxToChatbotRequestDto -> ChatbotRequestDto 변경 후 ChatbotService 호출
        ChatbotRequestDto chatbotRequestDto = ChatbotRequestDto.builder()
                .userId(requestDto.getUserId())
                .text(requestDto.getText())
                .build();
        log.info("[HcxToChatbotService] sendHcxToChatbot - chatbotRequestDto : {}", chatbotRequestDto);

        return clovaChatbotService.sendChatbotMessage(url, secretKey, chatbotRequestDto, "send");
    }

    public String customApi(HttpServletRequest req) {
        String responseStatus = "true"; // 커스텀 조건에 보내줄 상태 값
        String body = "";

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        /* request의 body 값을 읽어온다. */
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
            } else {
                stringBuilder.append("");
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
        log.warn(body);

        /* 커스텀 조건 응답 JSON */
        JSONObject resJson = new JSONObject();
        JSONObject resArrJson = new JSONObject();
        JSONArray arrJson = new JSONArray();

        resArrJson.put("name", "hcx_result_code");  // 사용자 변수명
        resArrJson.put("value", "2000"); // 값, 텍스트나 숫자가 올 수 있음.
        resArrJson.put("type", "TEXT"); // userVariable의 타입 명시. 문자열 타입일 경우 TEXT, 숫자 타입일 경우 NUMBER 를 입력
        resArrJson.put("action", "EQ"); // 동작을 지정한다. EQ, ADD, SUB가 올 수 있으며, 문자열 타입일 경우 EQ만 가능
        resArrJson.put("valueType", "TEXT"); // userVariable.value 의 타입이 무엇인지 명시.

        arrJson.put(resArrJson);

        resJson.put("valid", responseStatus); // 해당 태그가 유효한지 파악한다,
        resJson.put("userVariable", arrJson); // 사용자 변수를 변경하기 위해 사용 가능

        log.info("JSON 데이터 테스트 : " + resJson);

        return resJson.toString();
    }

}
