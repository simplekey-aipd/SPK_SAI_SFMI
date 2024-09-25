package com.minjeong.chatbotapigateway.hcx.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/*
    ### Chatbot 의 사용자 변수 값 저장
 */
@Slf4j
@Service
public class SetChatbotResponseService {
    /*
    default user variables
        - hcx_result_code : HCX API 응답 코드
        - retry_count : HCX API 요청 시도 횟수
     */

    /*
    1. HCX 로 주소 넘겨준 뒤, 전체 주소 값 response 로 받아서 저장
        - user variable : address
     */
    public String setChatbotResponseAddressByHcxResponse(JSONObject hcxResponse) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        JSONObject data = hcxResponse.getJSONObject("data");

        // 주소
        JSONObject userVariableJsonObj_addr = new JSONObject();
        userVariableJsonObj_addr.put("name", "address");
        userVariableJsonObj_addr.put("value", data.getString("addr"));
        userVariableJsonObj_addr.put("type", "TEXT");
        userVariableJsonObj_addr.put("action", "EQ");
        userVariableJsonObj_addr.put("valueType", "TEXT");

        userVariableJsonArray.put(userVariableJsonObj_addr);
        userVariableJsonArray.put(setUserVariable_hcxResultCode(hcxResponse));
        userVariableJsonArray.put(setUserVariable_retryCount(hcxResponse));

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        log.info("[getAddress] - responseJson : {}", resJson);
        log.warn("[getAddress] - time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return resJson.toString();
    }

    public String setChatbotResponseDetailAddressByHcxResponse(JSONObject hcxResponse) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        JSONObject data = hcxResponse.getJSONObject("data");

        // 상세 주소
        JSONObject userVariableJsonObj_detail_addr = new JSONObject();
        userVariableJsonObj_detail_addr.put("name", "detail_address");
        userVariableJsonObj_detail_addr.put("value", data.getString("detail_addr"));
        userVariableJsonObj_detail_addr.put("type", "TEXT");
        userVariableJsonObj_detail_addr.put("action", "EQ");
        userVariableJsonObj_detail_addr.put("valueType", "TEXT");

        userVariableJsonArray.put(userVariableJsonObj_detail_addr);
        userVariableJsonArray.put(setUserVariable_hcxResultCode(hcxResponse));
        userVariableJsonArray.put(setUserVariable_retryCount(hcxResponse));

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        log.info("[getDetailAddress] - responseJson : {}", resJson);
        log.warn("[getDetailAddress] - time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return resJson.toString();
    }

    public String setChatbotResponseDateByHcxResponse(JSONObject hcxResponse) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        int schedule_len = hcxResponse.getJSONObject("data").length() -1;
        for (int i = 0; i < schedule_len; i++) {
            String schedule_text = hcxResponse.getJSONObject("data").getJSONObject("schedule" + (i + 1)).getString("text");
            String schedule_data = hcxResponse.getJSONObject("data").getJSONObject("schedule" + (i + 1)).getString("data");
            String schedule_data_12 = "";
            int hour = Integer.parseInt(schedule_data.split(":")[0]);
            int minute = Integer.parseInt(schedule_data.split(":")[1]);
            if (hour < 12) {
                schedule_data_12 = String.format("%02d:%02d", hour + 12, minute);
            } else {
                schedule_data_12 = String.format("%02d:%02d", hour - 12, minute);
            }

            String variableName = "schedule" + (i + 1);
            log.info("[getSchedules] - schedule_text : {}, schedule_data : {}", schedule_text, schedule_data);

            JSONObject userVariableJsonObj_schedule_data = new JSONObject();
            userVariableJsonObj_schedule_data.put("name", variableName + "_data");
            userVariableJsonObj_schedule_data.put("value", schedule_data);
            userVariableJsonObj_schedule_data.put("type", "TEXT");
            userVariableJsonObj_schedule_data.put("action", "EQ");
            userVariableJsonObj_schedule_data.put("valueType", "TEXT");

            JSONObject userVariableJsonObj_schedule_data_12 = new JSONObject();
            userVariableJsonObj_schedule_data_12.put("name", variableName + "_data_12");
            userVariableJsonObj_schedule_data_12.put("value", schedule_data_12);
            userVariableJsonObj_schedule_data_12.put("type", "TEXT");
            userVariableJsonObj_schedule_data_12.put("action", "EQ");
            userVariableJsonObj_schedule_data_12.put("valueType", "TEXT");

            JSONObject userVariableJsonObj_schedule_text = new JSONObject();
            userVariableJsonObj_schedule_text.put("name", variableName + "_text");
            userVariableJsonObj_schedule_text.put("value", schedule_text);
            userVariableJsonObj_schedule_text.put("type", "TEXT");
            userVariableJsonObj_schedule_text.put("action", "EQ");
            userVariableJsonObj_schedule_text.put("valueType", "TEXT");

            userVariableJsonArray.put(userVariableJsonObj_schedule_data);
            userVariableJsonArray.put(userVariableJsonObj_schedule_data_12);
            userVariableJsonArray.put(userVariableJsonObj_schedule_text);
        }
        userVariableJsonArray.put(setUserVariable_hcxResultCode(hcxResponse));
        userVariableJsonArray.put(setUserVariable_retryCount(hcxResponse));

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);


        log.info("[getSchedules] - responseJson : {}", resJson);
        return resJson.toString();
    }

    public String setChatbotResponseAvailableScheduleByHcxResponse(JSONObject hcxResponse) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        JSONObject data = hcxResponse.getJSONObject("data");
        /*
        reservation : false (예약 불가)
        data.schedule1.text, data.schedule1.data, reservation, retry_count 추출

        reservation : true (예약 가능)
        reservation, retry_count 추출
         */
        if (!data.getBoolean("reservation")) {
            String schedule_text = data.getJSONObject("schedule1").getString("text");
            String schedule_data = data.getJSONObject("schedule1").getString("data");
            String schedule_data_12 = "";

            int hour = Integer.parseInt(schedule_data.split(":")[0]);
            int minute = Integer.parseInt(schedule_data.split(":")[1]);
            if (hour < 12) {
                schedule_data_12 = String.format("%02d:%02d", hour + 12, minute);
            } else {
                schedule_data_12 = String.format("%02d:%02d", hour - 12, minute);
            }

            JSONObject userVariableJsonObj_schedule_data = new JSONObject();
            userVariableJsonObj_schedule_data.put("name", "schedule1_data");
            userVariableJsonObj_schedule_data.put("value", schedule_data);
            userVariableJsonObj_schedule_data.put("type", "TEXT");
            userVariableJsonObj_schedule_data.put("action", "EQ");
            userVariableJsonObj_schedule_data.put("valueType", "TEXT");

            JSONObject userVariableJsonObj_schedule_data_12 = new JSONObject();
            userVariableJsonObj_schedule_data_12.put("name", "schedule1_data_12");
            userVariableJsonObj_schedule_data_12.put("value", schedule_data_12);
            userVariableJsonObj_schedule_data_12.put("type", "TEXT");
            userVariableJsonObj_schedule_data_12.put("action", "EQ");
            userVariableJsonObj_schedule_data_12.put("valueType", "TEXT");

            JSONObject userVariableJsonObj_schedule_text = new JSONObject();
            userVariableJsonObj_schedule_text.put("name", "schedule1_text");
            userVariableJsonObj_schedule_text.put("value", schedule_text);
            userVariableJsonObj_schedule_text.put("type", "TEXT");
            userVariableJsonObj_schedule_text.put("action", "EQ");
            userVariableJsonObj_schedule_text.put("valueType", "TEXT");

            userVariableJsonArray.put(userVariableJsonObj_schedule_data);
            userVariableJsonArray.put(userVariableJsonObj_schedule_data_12);
            userVariableJsonArray.put(userVariableJsonObj_schedule_text);
        }

        userVariableJsonArray.put(setUserVariable_hcxResultCode(hcxResponse));
        userVariableJsonArray.put(setUserVariable_retryCount(hcxResponse));

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);
        return resJson.toString();
    }

    public String setChatbotResponseDefaultByHcxResponse(JSONObject hcxResponse) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        userVariableJsonArray.put(setUserVariable_hcxResultCode(hcxResponse));
        userVariableJsonArray.put(setUserVariable_retryCount(hcxResponse));

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        log.info("[setDefaultResp] - responseJson : {}", resJson);
        log.warn("[setDefaultResp] - time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return resJson.toString();
    }

    public String setChatbotResponseScheduleCustom(String time) {
        log.info("[setChatbotResponseScheduleCustom] Custom Time : " + time);
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        JSONObject resArrJson = new JSONObject();
        resArrJson.put("name", "time");
        resArrJson.put("value", time);
        resArrJson.put("type", "TEXT");
        resArrJson.put("action", "EQ");
        resArrJson.put("valueType", "TEXT");

        JSONObject resArrJson1 = new JSONObject();
        resArrJson1.put("name", "hour");
        resArrJson1.put("value", time.split(":")[0]);
        resArrJson.put("type", "TEXT");
        resArrJson.put("action", "EQ");
        resArrJson.put("valueType", "TEXT");

        JSONObject resArrJson2 = new JSONObject();
        resArrJson2.put("name", "minute");
        resArrJson2.put("value", time.split(":")[1]);
        resArrJson.put("type", "TEXT");
        resArrJson.put("action", "EQ");
        resArrJson.put("valueType", "TEXT");

        userVariableJsonArray.put(resArrJson);
        userVariableJsonArray.put(resArrJson1);
        userVariableJsonArray.put(resArrJson2);

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        return resJson.toString();
    }

    // 의도 파악
    public String setChatbotResponseIntentByHcxResponse(JSONObject hcxResponse) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        JSONObject data = hcxResponse.getJSONObject("data");
        log.info("[setChatbotResponseIntentByHcxResponse] - data : {}", data);

        JSONObject userVariableJsonObj_intent_code = new JSONObject();
        userVariableJsonObj_intent_code.put("name", "code");
        userVariableJsonObj_intent_code.put("value", data.getString("move_code"));
        userVariableJsonObj_intent_code.put("type", "TEXT");
        userVariableJsonObj_intent_code.put("action", "EQ");
        userVariableJsonObj_intent_code.put("valueType", "TEXT");

        JSONObject userVariableJsonObj_hcx_message = new JSONObject();
        userVariableJsonObj_hcx_message.put("name", "hcx_message");
        userVariableJsonObj_hcx_message.put("value", data.getString("hcx_message"));
        userVariableJsonObj_hcx_message.put("type", "TEXT");
        userVariableJsonObj_hcx_message.put("action", "EQ");
        userVariableJsonObj_hcx_message.put("valueType", "TEXT");

        userVariableJsonArray.put(userVariableJsonObj_intent_code);
        userVariableJsonArray.put(userVariableJsonObj_hcx_message);
        userVariableJsonArray.put(setUserVariable_hcxResultCode(hcxResponse));

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        log.info("[getIntentCode] - responseJson : {}", resJson);
        log.warn("[getIntentCode] - time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return resJson.toString();
    }

    // Exception
    // 예외 일 때 사용자 변수 저장
    public String setChatbotResponseExceptionByHcxResponse(JSONObject hcxResponse) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        JSONObject resArrJson = new JSONObject();
        resArrJson.put("name", "hcx_result_code");
        resArrJson.put("value", String.valueOf(hcxResponse.getInt("status_code")));
        resArrJson.put("type", "TEXT");
        resArrJson.put("action", "EQ");
        resArrJson.put("valueType", "TEXT");

        JSONObject resArrJson1 = new JSONObject();
        resArrJson1.put("name", "hcx_result_message");
        resArrJson1.put("value", hcxResponse.getString("message"));
        resArrJson1.put("type", "TEXT");
        resArrJson1.put("action", "EQ");
        resArrJson1.put("valueType", "TEXT");

        userVariableJsonArray.put(resArrJson);
        userVariableJsonArray.put(resArrJson1);

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        log.info("[getAddress] - responseJson : {}", resJson);
        log.warn("[getAddress] - time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return resJson.toString();
    }

    public JSONObject setUserVariable_hcxResultCode(JSONObject hcxResponse) {
        JSONObject userVariableJsonObj_hcx_result_code = new JSONObject();
        userVariableJsonObj_hcx_result_code.put("name", "hcx_result_code");
        userVariableJsonObj_hcx_result_code.put("value", String.valueOf(hcxResponse.getInt("status_code")));
        userVariableJsonObj_hcx_result_code.put("type", "TEXT");
        userVariableJsonObj_hcx_result_code.put("action", "EQ");
        userVariableJsonObj_hcx_result_code.put("valueType", "TEXT");
        return userVariableJsonObj_hcx_result_code;
    }

    public JSONObject setUserVariable_retryCount(JSONObject hcxResponse) {
        JSONObject userVariableJsonObj_retry_count = new JSONObject();
        userVariableJsonObj_retry_count.put("name", "retry_count");
        userVariableJsonObj_retry_count.put("value", hcxResponse.getJSONObject("data").getInt("retry_count"));
        userVariableJsonObj_retry_count.put("type", "NUMBER");
        userVariableJsonObj_retry_count.put("action", "EQ");
        userVariableJsonObj_retry_count.put("valueType", "NUMBER");
        return userVariableJsonObj_retry_count;
    }
}
