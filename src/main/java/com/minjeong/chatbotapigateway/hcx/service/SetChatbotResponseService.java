package com.minjeong.chatbotapigateway.hcx.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SetChatbotResponseService {

    public String setChatbotScheduleByHcxResponse(JSONObject hcxResponse) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        for (int i = 0; i < 3; i++) {
            String schedule_text = hcxResponse.getJSONObject("data").getJSONObject("schedule" + (i + 1)).getString("text");
            String schedule_data = hcxResponse.getJSONObject("data").getJSONObject("schedule" + (i + 1)).getString("data");
            String variableName = "schedule" + (i + 1);

            JSONObject resArrJson = new JSONObject();
            resArrJson.put("name", variableName);
            resArrJson.put("value", schedule_data);
            resArrJson.put("type", "TEXT");
            resArrJson.put("action", "EQ");
            resArrJson.put("valueType", "TEXT");

            JSONObject resArrJson1 = new JSONObject();
            resArrJson1.put("name", variableName + "_text");
            resArrJson1.put("value", schedule_text);
            resArrJson1.put("type", "TEXT");
            resArrJson1.put("action", "EQ");
            resArrJson1.put("valueType", "TEXT");

            userVariableJsonArray.put(resArrJson);
            userVariableJsonArray.put(resArrJson1);
        }
        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        log.info("[getSchedules] - responseJson : {}", resJson);
        return resJson.toString();
    }

    /*
        {
            "status_code": 2000,
            "message": "OK",
            "data": {
                "addr": "서울특별시 송파구 삼전동 백제고분로28길 28-12",
                "retry_count": 0,
        }
}
         */
    public String setChatbotAddressByHcxResponse(JSONObject hcxResponse) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        JSONObject data = hcxResponse.getJSONObject("data");
        // 주소
        JSONObject resArrJson = new JSONObject();
        resArrJson.put("name", "address");
        resArrJson.put("value", data.getString("addr"));
        resArrJson.put("type", "TEXT");
        resArrJson.put("action", "EQ");
        resArrJson.put("valueType", "TEXT");

        // retry count
        JSONObject resArrJson1 = new JSONObject();
        resArrJson1.put("name", "retry_count");
        resArrJson1.put("value", data.getInt("retry_count"));
        resArrJson1.put("type", "TEXT");
        resArrJson1.put("action", "EQ");
        resArrJson1.put("valueType", "TEXT");

        userVariableJsonArray.put(resArrJson);
        userVariableJsonArray.put(resArrJson1);

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);

        log.info("[getAddress] - responseJson : {}", resJson);
        return resJson.toString();
    }

    // 예외 일 때 사용자 변수 저장
    public String setChatbotExceptionByHcxResponse(JSONObject hcxResponse) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        JSONObject resArrJson = new JSONObject();
        resArrJson.put("name", "hcx_result_code");
        resArrJson.put("value", hcxResponse.getInt("status_code"));
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
        return resJson.toString();
    }
}
