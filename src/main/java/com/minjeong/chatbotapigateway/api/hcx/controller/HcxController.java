package com.minjeong.chatbotapigateway.api.hcx.controller;

import com.minjeong.chatbotapigateway.api.hcx.service.GetInfoByChatbotResponseService;
import com.minjeong.chatbotapigateway.api.hcx.service.HcxTestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/hcx/v1")
public class HcxController {

    private final GetInfoByChatbotResponseService getInfoByChatbotResponseService;
    private final HcxTestService hcxTestService;

    // 1. 주소 요청 및 등록
    @PostMapping("/setAddress")
    public String address(HttpServletRequest req) {
        log.warn("[HCX API] - /setAddress | time(HH:mm:ss) : {}", java.time.LocalTime.now());
        return getInfoByChatbotResponseService.getAddressByHcxResp(req);
    }

    // 2. 상세 주소 요청 및 등록
    @PostMapping("/setDetailAddress")
    public String detailAddress(HttpServletRequest req) {
        log.warn("[HCX API] - /setDetailAddress | time(HH:mm:ss) : {}", java.time.LocalTime.now());
        return getInfoByChatbotResponseService.getDetailAddressByHcxResp(req);
    }

    // 3. 가능한 시간대 요청
    @PostMapping("/getSchedule")
    public String schedule(HttpServletRequest req) {
        log.warn("[HCX API] - /schedule | time(HH:mm:ss) : {}", java.time.LocalTime.now());
        return getInfoByChatbotResponseService.getScheduleByHcxResp(req);
    }

    // 3-1. 가능한 일정 요청
    @PostMapping("/getAvailableSchedule")
    public String getAvailableSchedule(HttpServletRequest req) {
        log.warn("[HCX API] - /getAvailableSchedule | time(HH:mm:ss) : {}", java.time.LocalTime.now());
        return getInfoByChatbotResponseService.getAvailableScheduleByHcxResp(req);
    }

    // 4. 방문 일정 확정
    @PostMapping("/setSchedule")
    public String setSchedule(HttpServletRequest req) {
        log.warn("[HCX API] - /setSchedule | time(HH:mm:ss) : {}", java.time.LocalTime.now());
        return getInfoByChatbotResponseService.setScheduleByHcxResp(req);
    }

    // 5. 사용자 정보
    @PostMapping("/setUserInfo")
    public String userInfo(HttpServletRequest req) {
        log.warn("[HCX API] - /setUserInfo | time(HH:mm:ss) : {}", java.time.LocalTime.now());
        return getInfoByChatbotResponseService.setUserInfo(req);
    }

    // 6. CUSTOM _ 시간대 9~18 시간으로 변경
    @PostMapping("/setScheduleCustom")
    public String setScheduleCustom(HttpServletRequest req) {
        log.warn("[HCX API] - /setScheduleCustom | time(HH:mm:ss) : {}", java.time.LocalTime.now());
        return getInfoByChatbotResponseService.setScheduleCustom(req);
    }

    // 7. 의도 파악
    @PostMapping("/getIntent")
    public String intent(HttpServletRequest req) {
        log.warn("[HCX API] - /intent | time(HH:mm:ss) : {}", java.time.LocalTime.now());
        return getInfoByChatbotResponseService.getIntentByHcxResp(req);
    }


    // ===============
    // TEST
    // 주소 response 테스트용
    @PostMapping("/test/setAddress")
    public String testSetAddress(HttpServletRequest req) {
        log.warn("[HCX API] - /test/setAddress | time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return hcxTestService.testSetAddressByHcxResp(req);
    }

    // 시간대 response 테스트용
    @PostMapping("/test/getSchedule")
    public String testGetSchedule(HttpServletRequest req) {
        log.warn("[HCX API] - /test/getSchedule | time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return hcxTestService.testGetSchedulesByHcxResp(req);
    }

    // 시간대 등록 response 테스트용
    @PostMapping("/test/setSchedule")
    public String testSetSchedules(HttpServletRequest req) {
        log.warn("[HCX API] - /test/setSchedules | time(HH:mm:ss) : {}, time(ms) : {}", java.time.LocalTime.now(), System.currentTimeMillis());
        return hcxTestService.testSetSchedulesByHcxResp(req);
    }
}
