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
    1. 사용자가 말한 주소로 유효성, 권역 확인 하는 API
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

        log.warn("[hcxService.sendPostRequestToHcx] - start | time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        JSONObject hcxResponse = hcxService.sendPostRequestToHcx(userId, category, data);
        log.warn("[hcxService.sendPostRequestToHcx] - end | time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());

        int statusCode = hcxResponse.getInt("status_code");
        if (statusCode == 2000) {
            log.info("statusCode : 2000 -> setChatbotAddressByHcxResponse");
            return setChatbotResponseService.setChatbotResponseAddressByHcxResponse(hcxResponse);
        } else {
            log.warn("statusCode : {} -> setChatbotExceptionByHcxResponse", statusCode);
            return setChatbotResponseService.setChatbotResponseExceptionByHcxResponse(hcxResponse);
        }
    }

    /*
    2. 사용자가 말한 상세 주소로 DB 에 등록되어 있는지 확인하는 API
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

        log.warn("[hcxService.sendPostRequestToHcx] - start | time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        JSONObject hcxResponse = hcxService.sendPostRequestToHcx(userId, category, data);
        log.warn("[hcxService.sendPostRequestToHcx] - end | time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());

        int statusCode = hcxResponse.getInt("status_code");

        if (statusCode == 2000) {
            log.warn("statusCode : 2000 -> setChatbotDetailAddressByHcxResponse");
            return setChatbotResponseService.setChatbotResponseDetailAddressByHcxResponse(hcxResponse);
        } else {
            log.warn("statusCode : {} -> setChatbotExceptionByHcxResponse", statusCode);
            return setChatbotResponseService.setChatbotResponseExceptionByHcxResponse(hcxResponse);
        }
    }

    /*
    3. 사용자가 요청한 날짜에 가능한 시간대 정보를 가져오는 API
        1) chatbot 에서 가져올 정보
            userId
            category : DATE
            date : 사용자가 선택한 날짜 - date(yyyy-mm-dd)
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
        String date = userVariables.getJSONObject("date").getString("value");
        String code = userVariables.getJSONObject("code").getString("value");

        Map<String, String> data = new HashMap<>();
        data.put("code", code);
        data.put("date", date);
        log.info("[getSchedules] - userId : {}, code : {}, date : {}", userId, code, date);

        // hcx 로 request 보내기
        JSONObject hcxResponse = hcxService.sendPostRequestToHcx(userId, category, data);
        
        // 하드 코딩으로 response 저장

        if (hcxResponse.getInt("status_code") == 2000) {
            log.info("statusCode : 2000 -> setChatbotDateByHcxResponse");
            // chatbot 으로 보낼 response
            return setChatbotResponseService.setChatbotResponseDateByHcxResponse(hcxResponse);
        } else {
            return setChatbotResponseService.setChatbotResponseExceptionByHcxResponse(hcxResponse);
        }
    }

    /*
    4. 사용자가 선택한 시간대로 방문 일정 확정하는 API
        1) chatbot 에서 가져올 정보
            userId
            category : SCHEDULE
            time : HH:MM
        2) hcx response 에서 가져올 정보
            retry_count : 재시도 횟수
     */
    public String setScheduleByHcxResp(HttpServletRequest req) {
        String body = hcxService.getChatbotRequestBody(req);

        // chatbot 에서 data 가져오기
        JSONObject reqJson = new JSONObject(body);
        String userId = reqJson.getString("userId");

        JSONObject userVariables = reqJson.getJSONObject("userVariables");
        String category = userVariables.getJSONObject("category").getString("value");
        String req_time_data = userVariables.getJSONObject("req_time_data").getString("value");

        Map<String, String> data = new HashMap<>();
        data.put("time", req_time_data);

        log.info("[setSchedules] - userId : {}, req_time_data : {}", userId, req_time_data);

        // hcx 로 request 보내기
        JSONObject hcxResponse = hcxService.sendPostRequestToHcx(userId, category, data);

        // 하드 코딩으로 response 저장

        if (hcxResponse.getInt("status_code") == 2000) {
            // chatbot 으로 보낼 response
            return setChatbotResponseService.setChatbotResponseDefaultByHcxResponse(hcxResponse);
        } else {
            return setChatbotResponseService.setChatbotResponseExceptionByHcxResponse(hcxResponse);
        }
    }

    /*
    5. 사용자 정보 등록 API
        1) chatbot 에서 가져올 정보
            userId
            category : USER
            name, phone, birth
        2) hcx response 에서 가져올 정보
            retry_count : 재시도 횟수
     */
    public String setUserInfo(HttpServletRequest req) {
        String body = hcxService.getChatbotRequestBody(req);

        // chatbot 에서 data 가져오기
        JSONObject reqJson = new JSONObject(body);
        String userId = reqJson.getString("userId");

        JSONObject userVariables = reqJson.getJSONObject("userVariables");
        String category = userVariables.getJSONObject("category").getString("value");
        String name = userVariables.getJSONObject("name").getString("value");
        String phone = userVariables.getJSONObject("phone").getString("value");
        String birth = userVariables.getJSONObject("birth").getString("value");

        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("phone", phone);
        data.put("birth", birth);

        // hcx 로 request 보내기
        JSONObject hcxResponse = hcxService.sendPostRequestToHcx(userId, category, data);

        // 하드 코딩으로 response 저장

        if (hcxResponse.getInt("status_code") == 2000) {
            // chatbot 으로 보낼 response
            return setChatbotResponseService.setChatbotResponseDefaultByHcxResponse(hcxResponse);
        } else {
            return setChatbotResponseService.setChatbotResponseExceptionByHcxResponse(hcxResponse);
        }
    }

    public String setScheduleCustom(HttpServletRequest req) {
        String body = hcxService.getChatbotRequestBody(req);

        // chatbot 에서 data 가져오기
        JSONObject reqJson = new JSONObject(body);
        String userId = reqJson.getString("userId");

        String time = reqJson.getJSONObject("userVariables").getJSONObject("time").getString("value");

        // 9~18 시 사이 값(영업시간)으로 변환
        if (Integer.parseInt(time.substring(0, 2)) < 9) {
            time = Integer.parseInt(time.substring(0, 2)) + 12 + time.substring(2);
        } else if (Integer.parseInt(time.substring(0, 2)) > 18) {
            time = Integer.parseInt(time.substring(0, 2)) - 12 + time.substring(2);
        }

        return setChatbotResponseService.setChatbotResponseScheduleCustom(time);
    }

    public String getIntentByHcxResp(HttpServletRequest req) {
        String body = hcxService.getChatbotRequestBody(req);

        JSONObject reqJson = new JSONObject(body);
        String userId = reqJson.getString("userId");

        JSONObject userVariables = reqJson.getJSONObject("userVariables");
        String category = userVariables.getJSONObject("category").getString("value");
        String user_intent = userVariables.getJSONObject("user_intent").getString("value");

        Map<String, String> data = new HashMap<>();
        data.put("message", user_intent);

        JSONObject hcxResponse = hcxService.sendPostRequestToHcx(userId, category, data);

        if (hcxResponse.getInt("status_code") == 2000) {
            return setChatbotResponseService.setChatbotResponseIntentByHcxResponse(hcxResponse);
        } else {
            return setChatbotResponseService.setChatbotResponseExceptionByHcxResponse(hcxResponse);
        }
    }
}
