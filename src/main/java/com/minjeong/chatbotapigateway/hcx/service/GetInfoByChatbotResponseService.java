package com.minjeong.chatbotapigateway.hcx.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetInfoByChatbotResponseService {

    private final HcxService hcxService;
    private final SetChatbotResponseService setChatbotResponseService;

    /*
    사용자가 요청한 날짜에 가능한 시간대 정보를 가져오는 API
    1) chatbot 에서 가져올 정보
        userId
        category : DATE
        date : 사용자가 선택한 날짜 - 전체날짜_yyyymmdd(yyyy-mm-dd)
        code : 전입(Z02)/전출(Z03)
    2) hcx response 에서 가져올 정보
        data.schedule(i).text : 사용자에게 보여줄 text
        data.schedule(i).data : 값 비교할 data
    */

    public String getScheduleByHcxResp(HttpServletRequest req) {
        String body = hcxService.getChatbotRequestBody(req);

        // chatbot 에서 data 가져오기
        JSONObject reqJson = new JSONObject(body);
        String userId = reqJson.getString("userId");

        JSONObject userVariables = reqJson.getJSONObject("userVariables");
        String category = userVariables.getJSONObject("category").getString("value");
        String code = userVariables.getJSONObject("code").getString("value");
        String date = userVariables.getJSONObject("전체날짜_yyyymmdd").getString("value");

        Map<String, String> data = new HashMap<>();
        data.put("code", code);
        data.put("date", date);
        log.info("[getSchedules] - userId : {}, code : {}, date : {}", userId, code, date);

        // hcx 로 request 보내기
        JSONObject hcxResponse = hcxService.sendPostRequestToHcx(userId, category, data);

        // TODO : hcxResponse 의 status_code 에 따라서 예외처리

        if (hcxResponse.getInt("status_code") == 2000) {
            // chatbot 으로 보낼 response
            return setChatbotResponseService.setChatbotScheduleByHcxResponse(hcxResponse);
        } else {
            return hcxResponse.getString("message");
        }
    }

    /*
    사용자가 말한 주소로 유효성, 권역 확인 하는 API
    1) chatbot 에서 가져올 정보
        userId
        category : DATE
        address : 주소
    2) hcx response 에서 가져올 정보
        data.addr : hcx 통해 검증된 주소
     */
    public String getAddressByHcxResp(HttpServletRequest req) {
        String chatbotRequestBody = hcxService.getChatbotRequestBody(req);

        JSONObject reqJson = new JSONObject(chatbotRequestBody);
        String userId = reqJson.getString("userId");

        JSONObject userVariables = reqJson.getJSONObject("userVariables");
        String category = userVariables.getJSONObject("category").getString("value");
        String address = userVariables.getJSONObject("address").getString("value");

        Map<String, String> data = new HashMap<>();
        data.put("addr", address);

        log.info("[getAddress] - userId : {}, address : {}", userId, address);

        JSONObject hcxResponse = hcxService.sendPostRequestToHcx(userId, category, data);

        // TODO : hcxResponse 의 status_code 에 따라서 예외처리
        int statusCode = hcxResponse.getInt("status_code");
        /*
            2000
            {
                "status_code": 2000,
                "message": "OK",
                "data": {
                        "addr": "서울특별시 송파구 삼전동 백제고분로28길 28-12",
                        "retry_count": 0,
                }
            }
            3001
            {
                "status_code" : 3001
                "message": "주소기반산업서비스에 일치하는 주소가 존재하지 않음"
            }
            3002
            {
                "status_code" : 3002
                "message": "관할 권역이 아닙니다."
            }
         */

        if (statusCode == 2000) {
            return setChatbotResponseService.setChatbotAddressByHcxResponse(hcxResponse);
        } else {
            return setChatbotResponseService.setChatbotExceptionByHcxResponse(hcxResponse);
        }
    }

    // 상세 주소

}
