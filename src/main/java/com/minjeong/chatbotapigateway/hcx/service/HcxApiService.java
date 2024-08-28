package com.minjeong.chatbotapigateway.hcx.service;

import com.minjeong.chatbotapigateway.hcx.dto.HcxRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
public class HcxApiService {

    public Object sendHcxMessage(HcxRequestDto dto) {

        String userId = dto.getUserId();
        String message = dto.getMsg();

        log.info("HCX Service - Request : " + message);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setConnection("keep-alive");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("msg", message);

        // HttpEntity 에 header 와 body 넣음
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // RestTemplate 을 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<String> response = restTemplate.exchange(
                "https://llm-ax.claion.io/CityGas",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        log.info("HcxApiService - Response : header = {}, body = {}", response.getHeaders(), response.getBody());
        return response.getBody();
    }
}
