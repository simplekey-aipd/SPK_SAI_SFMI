package com.simplekey.spk_sai_sfmi.chatbot.global;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class TestController {

    // TEST : 6초 후 response 보내는 무응답 테스트
    @GetMapping("/noResponse/{seconds}")
    public ResponseEntity<?> testNoResponse(@PathVariable int seconds) {
        try {
            log.info("No Response Test - milliseconds : {}", seconds);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = "No Response Test - " + seconds + " seconds";
        // json 형식으로 msg : "" 전달
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", result);
        return ResponseEntity.ok(jsonObject.toString());
    }

    // domain : llm-cx.io/api/test/customApi/addr
    // 1. chatbot 에서
    // {
    //  "userId": "string",
    //  "userKey": "string",
    //  "userVariables": {
    //    "addr": {
    //      "value": "양천로 738 이요",
    //      "typ": "TEXT"
    //    },
    //    "date": {
    //      "value": "{\"year\":2020, \"month\":6, \"day\":5, \"name\":\"olive\"}",
    //      "typ": "JSON"
    //    }
    //  }
    //}
    // 중 userId, userKey, userVariables.addr.value 추출

    // 2. HCX 로 보낼 request body 만들기
    // {
    //    "seq_num" : gid,
    //    "category" : "ADDR",
    //    "data" : {
    //        "addr" : "양천로 738 이요"
    //    }
    //}

    // 3. HCX response
    // {
    //    "status_code": 2000,
    //    "message": "OK",
    //    "data": {
    //            "addr": "서울특별시 송파구 삼전동 백제고분로28길 28-12",
    //            "retry_count": 0,
    //    }
    //} 에서 status_code, data.addr, data.retry_count 값 추출

    // category : addr, detail_addr, time
    @PostMapping("/customApi/{category}")
    public String testCoustom(@PathVariable String category, HttpServletRequest req) {
        log.info("customApi - category : {}", category);
        String responseStatus = "true";
        String body = "";

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
        log.warn(body);

        // req body 에서 userVariables.addr.value 추출
        JSONObject reqJson = new JSONObject(body);
        String userId = reqJson.getJSONObject("userId").toString();
        JSONObject userVariables = reqJson.getJSONObject("userVariables");
        String value = userVariables.getJSONObject(category).getString("value");

        // TODO : HCX response 값 추출 및 userVariable 값 변경

        JSONObject resJson = new JSONObject();
        JSONObject resArrJson = new JSONObject();
        JSONArray arrJson = new JSONArray();

        resArrJson.put("name", category);  // 사용자 변수명
        resArrJson.put("value", value); // 값, 텍스트나 숫자가 올 수 있음.
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
