package com.minjeong.chatbotapigateway.api.sfmi.controller;

import com.minjeong.chatbotapigateway.api.sfmi.service.SFMIApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sfmi/v1")
public class SFMIApiController {

    private final SFMIApiService sfmiApiService;

    // 긴급 출동 안내 정보 조회
    @GetMapping("/getEmergencyDispatchInfo")
    public ResponseEntity<?> getEmergencyDispatchInfo() {
        Object info = sfmiApiService.getEmergencyDispatchInfo();
        return ResponseEntity.ok(info);
    }
}
