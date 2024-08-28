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

}
