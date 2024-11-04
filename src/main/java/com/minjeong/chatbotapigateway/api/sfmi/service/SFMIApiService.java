package com.minjeong.chatbotapigateway.api.sfmi.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class SFMIApiService {

    // API 테스트 용
    public Object getEmergencyDispatchInfo() {

        String address = "서울 강서구 양천로 738";
        String address_detail = "407호";
        String car_no = "12가 5678";
        String type_cd = "1";
        String type_detail = "비상 급유";
        String contact_no = "01012345678";

        JSONObject result = new JSONObject();
        result.put("address", address);
        result.put("address_detail", address_detail);
        result.put("car_no", car_no);
        result.put("type_cd", type_cd);
        result.put("type_detail", type_detail);
        result.put("contact_no", contact_no);

        return result;
    }
}
