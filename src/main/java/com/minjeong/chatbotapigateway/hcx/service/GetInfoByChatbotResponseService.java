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
    사용자가 말한 주소로 유효성, 권역 확인 하는 API
        1) chatbot 에서 가져올 정보
            userId
            category : ADDR
            address : 주소
        2) hcx response 에서 가져올 정보
            data.addr : hcx 통해 검증된 주소
            retry_count : 재시도 횟수
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

        int statusCode = hcxResponse.getInt("status_code");

        if (statusCode == 2000) {
            return setChatbotResponseService.setChatbotAddressByHcxResponse(hcxResponse);
        } else {
            return setChatbotResponseService.setChatbotExceptionByHcxResponse(hcxResponse);
        }
    }

    /*
    사용자가 말한 상세 주소로 DB 에 등록되어 있는지 확인하는 API
        1) chatbot 에서 가져올 정보
            userId
            category : DETAIL_ADDR
            detail_address : 상세 주소
        2) hcx response 에서 가져올 정보
            data.detail_addr : hcx 통해 검증된 주소
            retry_count : 재시도 횟수
     */
    public String getDetailAddressByHcxResp(HttpServletRequest req) {
        String chatbotRequestBody = hcxService.getChatbotRequestBody(req);

        JSONObject reqJson = new JSONObject(chatbotRequestBody);
        String userId = reqJson.getString("userId");

        JSONObject userVariables = reqJson.getJSONObject("userVariables");
        String category = userVariables.getJSONObject("category").getString("value");
        String detailAddress = userVariables.getJSONObject("detail_address").getString("value");

        Map<String, String> data = new HashMap<>();
        data.put("detail_addr", detailAddress);

        log.info("[getDetailAddress] - userId : {}, detailAddress : {}", userId, detailAddress);

        JSONObject hcxResponse = hcxService.sendPostRequestToHcx(userId, category, data);

        int statusCode = hcxResponse.getInt("status_code");

        if (statusCode == 2000) {
            log.warn("statusCode : 2000 -> setChatbotDetailAddressByHcxResponse");
            return setChatbotResponseService.setChatbotDetailAddressByHcxResponse(hcxResponse);
        } else {
            log.warn("statusCode : {} -> setChatbotExceptionByHcxResponse", statusCode);
            return setChatbotResponseService.setChatbotExceptionByHcxResponse(hcxResponse);
        }
    }

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

        if (hcxResponse.getInt("status_code") == 2000) {
            // chatbot 으로 보낼 response
            return setChatbotResponseService.setChatbotScheduleByHcxResponse(hcxResponse);
        } else {
            return setChatbotResponseService.setChatbotExceptionByHcxResponse(hcxResponse);
        }
    }

}
