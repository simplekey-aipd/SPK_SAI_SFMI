package com.minjeong.chatbotapigateway.hcx.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HcxTestService {

    private final HcxService hcxService;

    // TEST
    // 주소 response 테스트용
    public String testSetAddressByHcxResp(HttpServletRequest req) {
        String chatbotRequestBody = hcxService.getChatbotRequestBody(req);

        JSONObject reqJson = new JSONObject(chatbotRequestBody);
        String userId = reqJson.getString("userId");

        JSONObject userVariables = reqJson.getJSONObject("userVariables");
        String category = userVariables.getJSONObject("category").getString("value");
        String address = userVariables.getJSONObject("address").getString("value");

        log.info("[testSetAddressByHcxResp] - userId : {}, category : {}, address : {}", userId, category, address);

        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        JSONObject userVariableJsonObj_address = new JSONObject();
        userVariableJsonObj_address.put("name", "address");
        userVariableJsonObj_address.put("value", "서울특별시 송파구 송파동  가락로 187");
        userVariableJsonObj_address.put("type", "TEXT");
        userVariableJsonObj_address.put("action", "EQ");
        userVariableJsonObj_address.put("valueType", "TEXT");

        JSONObject userVariableJsonObj_hcx_result_code = new JSONObject();
        userVariableJsonObj_hcx_result_code.put("name", "hcx_result_code");
        userVariableJsonObj_hcx_result_code.put("value", "2000");
        userVariableJsonObj_hcx_result_code.put("type", "TEXT");
        userVariableJsonObj_hcx_result_code.put("action", "EQ");
        userVariableJsonObj_hcx_result_code.put("valueType", "TEXT");

        JSONObject userVariableJsonObj_retry_count = new JSONObject();
        userVariableJsonObj_retry_count.put("name", "retry_count");
        userVariableJsonObj_retry_count.put("value", 2);
        userVariableJsonObj_retry_count.put("type", "NUMBER");
        userVariableJsonObj_retry_count.put("action", "EQ");
        userVariableJsonObj_retry_count.put("valueType", "NUMBER");

        userVariableJsonArray.put(userVariableJsonObj_address);
        userVariableJsonArray.put(userVariableJsonObj_hcx_result_code);
        userVariableJsonArray.put(userVariableJsonObj_retry_count);

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        log.info("[TEST] [setAddress] - responseJson : {}", resJson);
        log.warn("[TEST] [setAddress] - time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return resJson.toString();
    }

    // 시간대 response 테스트용
    public String testGetSchedulesByHcxResp(HttpServletRequest req) {
        String chatbotRequestBody = hcxService.getChatbotRequestBody(req);

        JSONObject reqJson = new JSONObject(chatbotRequestBody);
        String userId = reqJson.getString("userId");

        JSONObject userVariables = reqJson.getJSONObject("userVariables");
        String category = userVariables.getJSONObject("category").getString("value");
        String address = userVariables.getJSONObject("date").getString("value");
        String code = userVariables.getJSONObject("code").getString("value");


        log.info("[testGetSchedulesByHcxResp] - userId : {}, category : {}, date : {}, code : {}", userId, category, address, code);
        JSONObject testHcxResponse = new JSONObject();
        testHcxResponse.put("status_code", 2000);
        testHcxResponse.put("message", "OK");

        JSONObject data = new JSONObject();
        JSONObject schedule1 = new JSONObject();
        schedule1.put("text", "아홉시삼십분");
        schedule1.put("data", "09:30");

        JSONObject schedule2 = new JSONObject();
        schedule2.put("text", "열한시");
        schedule2.put("data", "11:00");

        JSONObject schedule3 = new JSONObject();
        schedule3.put("text", "십오시삼십분");
        schedule3.put("data", "15:30");

        data.put("schedule1", schedule1);
        data.put("schedule2", schedule2);
        data.put("schedule3", schedule3);
        data.put("retry_count", 0);

        testHcxResponse.put("data", data);

        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        // response.getJsonObject("data") 의 개수
        int dataCount = testHcxResponse.getJSONObject("data").length() - 1;
        log.info("[testGetSchedulesByHcxResp] - dataCount : {}", dataCount);
        // schedule1_text
        for (int i = 1; i <= dataCount; i++) {
            JSONObject userVariableJsonObj_schedule = new JSONObject();
            userVariableJsonObj_schedule.put("name", "schedule" + i + "_text");
            userVariableJsonObj_schedule.put("value", testHcxResponse.getJSONObject("data").getJSONObject("schedule" + i).getString("text"));
            userVariableJsonObj_schedule.put("type", "TEXT");
            userVariableJsonObj_schedule.put("action", "EQ");
            userVariableJsonObj_schedule.put("valueType", "TEXT");

            userVariableJsonArray.put(userVariableJsonObj_schedule);
        }

        // schedule1_data
        for (int i = 1; i <= dataCount; i++) {
            JSONObject userVariableJsonObj_schedule = new JSONObject();
            userVariableJsonObj_schedule.put("name", "schedule" + i + "_data");
            userVariableJsonObj_schedule.put("value", testHcxResponse.getJSONObject("data").getJSONObject("schedule" + i).getString("data"));
            userVariableJsonObj_schedule.put("type", "TEXT");
            userVariableJsonObj_schedule.put("action", "EQ");
            userVariableJsonObj_schedule.put("valueType", "TEXT");

            userVariableJsonArray.put(userVariableJsonObj_schedule);
        }

        JSONObject userVariableJsonObj_hcx_result_code = new JSONObject();
        userVariableJsonObj_hcx_result_code.put("name", "hcx_result_code");
        userVariableJsonObj_hcx_result_code.put("value", "2000");
        userVariableJsonObj_hcx_result_code.put("type", "TEXT");
        userVariableJsonObj_hcx_result_code.put("action", "EQ");
        userVariableJsonObj_hcx_result_code.put("valueType", "TEXT");

        JSONObject userVariableJsonObj_retry_count = new JSONObject();
        userVariableJsonObj_retry_count.put("name", "retry_count");
        userVariableJsonObj_retry_count.put("value", 2);
        userVariableJsonObj_retry_count.put("type", "NUMBER");
        userVariableJsonObj_retry_count.put("action", "EQ");
        userVariableJsonObj_retry_count.put("valueType", "NUMBER");

        userVariableJsonArray.put(userVariableJsonObj_hcx_result_code);
        userVariableJsonArray.put(userVariableJsonObj_retry_count);

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        log.info("[TEST] [testGetSchedulesByHcxResp] - responseJson : {}", resJson);
        log.warn("[TEST] [testGetSchedulesByHcxResp] - time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return resJson.toString();
    }

    // 가능한 시간대 확인 response 테스트용
    public String testSetSchedulesByHcxResp(HttpServletRequest req) {
        String chatbotRequestBody = hcxService.getChatbotRequestBody(req);

        JSONObject reqJson = new JSONObject(chatbotRequestBody);
        String userId = reqJson.getString("userId");

        JSONObject userVariables = reqJson.getJSONObject("userVariables");
        String category = userVariables.getJSONObject("category").getString("value");
        String address = userVariables.getJSONObject("req_time").getString("value");


        log.info("[testConfirmScheduleByHcxResp] - userId : {}, category : {}, date : {}", userId, category, address);
        JSONObject testHcxResponse = new JSONObject();

        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        JSONObject userVariableJsonObj_hcx_result_code = new JSONObject();
        userVariableJsonObj_hcx_result_code.put("name", "hcx_result_code");
        userVariableJsonObj_hcx_result_code.put("value", "2000");
        userVariableJsonObj_hcx_result_code.put("type", "TEXT");
        userVariableJsonObj_hcx_result_code.put("action", "EQ");
        userVariableJsonObj_hcx_result_code.put("valueType", "TEXT");

        JSONObject userVariableJsonObj_retry_count = new JSONObject();
        userVariableJsonObj_retry_count.put("name", "retry_count");
        userVariableJsonObj_retry_count.put("value", 2);
        userVariableJsonObj_retry_count.put("type", "NUMBER");
        userVariableJsonObj_retry_count.put("action", "EQ");
        userVariableJsonObj_retry_count.put("valueType", "NUMBER");

        userVariableJsonArray.put(userVariableJsonObj_hcx_result_code);
        userVariableJsonArray.put(userVariableJsonObj_retry_count);

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        log.info("[TEST] [testConfirmScheduleByHcxResp] - responseJson : {}", resJson);
        log.warn("[TEST] [testConfirmScheduleByHcxResp] - time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return resJson.toString();
    }
}
