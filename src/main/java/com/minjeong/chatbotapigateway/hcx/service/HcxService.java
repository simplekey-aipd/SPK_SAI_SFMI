package com.minjeong.chatbotapigateway.hcx.service;

import com.minjeong.chatbotapigateway.hcx.dto.HcxRequestDto;
import com.minjeong.chatbotapigateway.hcx.dto.HcxResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class HcxService {

    public Object hcx(HcxRequestDto dto) throws IOException {
        log.info("HCX Service - Request : " + dto.getMsg());

        String apiUrl = "https://llm-ax.claion.io/CityGas";
        String boundary = "----WebKitFormBoundary" + Long.toHexString(System.currentTimeMillis());

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=<calculated when request is sent>");
        conn.setDoOutput(true);


        JSONObject object = new JSONObject();
        object.put("msg", dto.getMsg());

        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.writeBytes("--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"msg\"\r\n");
            wr.writeBytes("Content-Type: text/plain; charset=UTF-8\r\n\r\n");
            wr.writeBytes(dto.getMsg() + "\r\n");
            wr.writeBytes("--" + boundary + "--\r\n");
            wr.flush();
        }

        log.info("Response Code : " + conn.getResponseCode());

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            builder.append(line);
        }
        br.close();

        JSONObject response = new JSONObject(builder.toString());
        log.info("Response : " + response);

        return new HcxResponseDto(response.getString("aimessage"));
    }

    public String getChatbotRequestBody(HttpServletRequest req) {
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
        log.info("[getChatbotRequestBody] - body : {}", body);
        return body;
    }

    public JSONObject sendPostRequestToHcx(String userId, String category, Map<String, String> data) {
        String url = "https://llm-ax.claion.io/skens/api";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("seq_num", userId);
        requestBody.put("category", category);
        requestBody.put("data", data);

        RestTemplate restTemplate = new RestTemplate();

        JSONObject response;
        try {
            response = new JSONObject(restTemplate.postForObject(url, requestBody, String.class));
            log.info("[SendPostRequest] - response : {}", response);

        } catch (Exception e) {
            log.error("[SendPostRequest] - error : {}", e.getMessage());
            response = new JSONObject("{\"status_code\": 5000, \"message\": \"ChatbotApiGateway - Internal Server Error\"}");
        }
        return response;
    }
}
