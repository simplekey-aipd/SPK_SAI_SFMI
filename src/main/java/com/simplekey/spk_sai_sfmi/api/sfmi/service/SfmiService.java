package com.simplekey.spk_sai_sfmi.api.sfmi.service;

import com.simplekey.spk_sai_sfmi.api.sfmi.mpc.domain.Twb_bas03;
import com.simplekey.spk_sai_sfmi.api.sfmi.mpc.repository.Twb_bas03Repository;
import com.simplekey.spk_sai_sfmi.api.sfmi.scm.domain.SfmiTargetRecord;
import com.simplekey.spk_sai_sfmi.api.sfmi.scm.repository.SfmiTargetRecordRepository;
import com.simplekey.spk_sai_sfmi.chatbot.clova.service.ClovaChatbotService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SfmiService {

    private final ClovaChatbotService clovaChatbotService;
    private final SfmiTargetRecordRepository sfmiTargetRecordRepository;
    private final Twb_bas03Repository twb_bas03Repository;

    // controller 에서 sid 받아옴 -> sid 로 SfmiTargetRecord 조회
    // -> SfmiTargetRecord.csrc_cd 로 Twb_bas03.cd_nm 조회
    // csrc_cd 로 조회할 경우 Twb_bas03 의 group_cd = 'WEB202' 이고 cen_type_cd = 'f1_ssf'
    // type_cd 로 조회할 경우 Twb_bas03 의 group_cd = 'WEB001' 이고 cen_type_cd = 'f1_ssf'

    public SfmiTargetRecord getTargetRecordBySid(String sid) {
        return sfmiTargetRecordRepository.findBySid(sid);
    }

    // 1) csrc_cd : Twb_bas03 에서 cen_type_cd = 'f1_ssf' 이고 group_cd = 'WEB202' 이고 cd = {csrc_cd} 인 cd_nm 을 리턴
    public String getCsrcNm(SfmiTargetRecord record) {
        log.info("getCsrcNm");
        return twb_bas03Repository.findByCenTypeCdAndGroupCdAndCd("f1_ssf", "WEB202", record.getCsrcCd())
                .map(Twb_bas03::getCdNm)
                .orElseThrow(() -> new IllegalArgumentException("해당 csrc_cd 가 없습니다."));
    }

    // 2) type_cd : Twb_bas03 에서 cen_type_cd = 'f1_ssf' 이고 group_cd = 'WEB001' 이고 cd = {type_cd} 인 cd_nm 을 리턴
    public String getTypeNm(SfmiTargetRecord record) {
        log.info("getTypeNm");
        return twb_bas03Repository.findByCenTypeCdAndGroupCdAndCd("f1_ssf", "WEB001", record.getTypeCd())
                .map(Twb_bas03::getCdNm)
                .orElseThrow(() -> new IllegalArgumentException("해당 type_cd 가 없습니다."));
    }

    public String fetchData(HttpServletRequest request) {
        // request 확인 header
        log.info("[fetchData] Request : {}, {}, {}", request.getPathInfo(), request.getQueryString(), request.getRemoteAddr());
        String requestBody = clovaChatbotService.getRequestBody(request);

        // ip 주소 확인
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr(); // 직접 연결된 클라이언트의 IP 주소
        }

        // X-Forwarded-For 헤더에 여러 IP가 포함될 경우, 첫 번째 IP 추출
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        log.warn("ipAddress : {}", ipAddress);

        JSONObject userVariables = new JSONObject(requestBody).getJSONObject("userVariables");
        String sid = userVariables.getJSONObject("sid").getString("value");
        SfmiTargetRecord record = getTargetRecordBySid(sid);

        String csrcNm = getCsrcNm(record);
        String typeNm = getTypeNm(record);
        log.info("csrcNm : {}, typeNm : {}", csrcNm, typeNm);
        return setChatbotParameter(record, csrcNm, typeNm);
    }

    public String fetchData2(String sid) {
        log.info("fetchData2");
        SfmiTargetRecord record = getTargetRecordBySid(sid);

        String csrcNm = getCsrcNm(record);
        String typeNm = getTypeNm(record);
        log.info("csrcNm : {}, typeNm : {}", csrcNm, typeNm);
        return setChatbotParameter(record, csrcNm, typeNm);
    }

    private String setChatbotParameter(SfmiTargetRecord record, String csrcNm, String typeNm) {
        JSONObject resJson = new JSONObject();
        JSONArray userVariableJsonArray = new JSONArray();

        // csrc_nm, type_nm, car_nm, address
        JSONObject userVariableJsonObj_csrcNm = new JSONObject();
        userVariableJsonObj_csrcNm.put("name", "csrc_cd");
        userVariableJsonObj_csrcNm.put("value", csrcNm);
        userVariableJsonObj_csrcNm.put("type", "TEXT");
        userVariableJsonObj_csrcNm.put("action", "EQ");
        userVariableJsonObj_csrcNm.put("valueType", "TEXT");

        JSONObject userVariableJsonObj_typeNm = new JSONObject();
        userVariableJsonObj_typeNm.put("name", "type_cd");
        userVariableJsonObj_typeNm.put("value", typeNm);
        userVariableJsonObj_typeNm.put("type", "TEXT");
        userVariableJsonObj_typeNm.put("action", "EQ");
        userVariableJsonObj_typeNm.put("valueType", "TEXT");

        JSONObject userVariableJsonObj_carNm = new JSONObject();
        userVariableJsonObj_carNm.put("name", "car_nm");
        userVariableJsonObj_carNm.put("value", record.getCarNm());
        userVariableJsonObj_carNm.put("type", "TEXT");
        userVariableJsonObj_carNm.put("action", "EQ");
        userVariableJsonObj_carNm.put("valueType", "TEXT");

        JSONObject userVariableJsonObj_address = new JSONObject();
        userVariableJsonObj_address.put("name", "address");
        userVariableJsonObj_address.put("value", record.getAddress());
        userVariableJsonObj_address.put("type", "TEXT");
        userVariableJsonObj_address.put("action", "EQ");
        userVariableJsonObj_address.put("valueType", "TEXT");

        userVariableJsonArray.put(userVariableJsonObj_csrcNm);
        userVariableJsonArray.put(userVariableJsonObj_typeNm);
        userVariableJsonArray.put(userVariableJsonObj_carNm);
        userVariableJsonArray.put(userVariableJsonObj_address);

        resJson.put("valid", "true");
        resJson.put("userVariable", userVariableJsonArray);
        return resJson.toString();
    }
}
