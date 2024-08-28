package com.minjeong.chatbotapigateway.hcx.controller;

import com.minjeong.chatbotapigateway.hcx.dto.HcxRequestDto;
import com.minjeong.chatbotapigateway.hcx.service.GetInfoByChatbotResponseService;
import com.minjeong.chatbotapigateway.hcx.service.HcxApiService;
import com.minjeong.chatbotapigateway.hcx.service.HcxService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/hcx/v1")
public class HcxController {

    private final HcxApiService hcxApiService;
    private final GetInfoByChatbotResponseService getInfoByChatbotResponseService;

    @PostMapping("/hcx")
    public ResponseEntity<?> hcx(@RequestBody HcxRequestDto dto) {
        return ResponseEntity.ok(hcxApiService.sendHcxMessage(dto));
//        return ResponseEntity.ok(hcxService.hcx(dto));
    }

    // 시간대 요청 및 등록
    @PostMapping("/schedule")
    public String schedule(HttpServletRequest req) {
        return getInfoByChatbotResponseService.getScheduleByHcxResp(req);
    }

    // 주소 요청 및 등록
    @PostMapping("/setAddress")
    public String address(HttpServletRequest req) {
        return getInfoByChatbotResponseService.getAddressByHcxResp(req);
    }

    // 상세 주소 요청 및 등록
    @PostMapping("/setDetailAddress")
    public String detailAddress(HttpServletRequest req) {
        return getInfoByChatbotResponseService.getDetailAddressByHcxResp(req);
    }
}
