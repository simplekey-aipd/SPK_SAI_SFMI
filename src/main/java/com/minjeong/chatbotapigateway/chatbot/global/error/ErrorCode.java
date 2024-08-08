package com.minjeong.chatbotapigateway.chatbot.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 100 (informational response)
    VERSION_NOT_SUPPORTED(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, 100, 1000, "지원하지 않는 버전입니다."),
    NOT_FOUND_DOMAIN_CODE(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, 100, 1001, "도메인 코드를 찾을 수 없습니다."),
    CHECK_URL_PARAM_IS_INVALID(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, 100, 1002, "URL 파라미터가 올바르지 않습니다."),

    // 400 (client error)
    REQUEST_PARAM_INVALID(HttpStatus.BAD_REQUEST, 400, 4000, "요청 파라미터가 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, 4010, "인증되지 않은 사용자입니다."),
    FORBIDDEN_TO_ACCESS(HttpStatus.FORBIDDEN, 403, 4030, "접근 권한이 없습니다."),
    SIGNATURE_VALIDATE_FAILED(HttpStatus.FORBIDDEN, 403, 4031, "서명 검증에 실패했습니다."),
    TIMESTAMP_EXCEEDED_TIME_WINDOW(HttpStatus.FORBIDDEN, 403, 4032, "타임스탬프가 유효하지 않습니다."),

    // 500 (server error)
    UNKNOWN_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, 5000, "알 수 없는 서비스 오류가 발생했습니다."),
    CURRENT_PROTOCOL_VERSION_NOT_SUPPORT_THIS_REPLY_STRUCTURE(HttpStatus.NOT_IMPLEMENTED, 501, 5010, "현재 프로토콜 버전에서는 이 응답 구조를 지원하지 않습니다."),
    CALLS_TO_THIS_API_HAVE_EXCEEDED_THE_RATE_LIMIT(HttpStatus.BAD_GATEWAY, 502, 5020, "이 API에 대한 호출이 요청 제한을 초과했습니다.");

    private final HttpStatus httpStatus;
    private final int httpCode;
    private final int ncloudCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, int httpCode, int ncloudCode, String message) {
        this.httpStatus = httpStatus;
        this.httpCode = httpCode;
        this.ncloudCode = ncloudCode;
        this.message = message;
    }
}
