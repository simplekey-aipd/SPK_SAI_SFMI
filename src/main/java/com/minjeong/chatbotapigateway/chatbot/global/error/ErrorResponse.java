package com.minjeong.chatbotapigateway.chatbot.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {

    private int httpStatusCode;
    private int ncloudErrorCode;
    private String errorMessage;

    // BindingResult 객체를 받아서 ErrorResponse 객체를 생성하는 메소드
    public static ErrorResponse of(int httpStatusCode, int ncloudErrorCode, String errorMessage) {
        return ErrorResponse.builder()
                .httpStatusCode(httpStatusCode)
                .ncloudErrorCode(ncloudErrorCode)
                .errorMessage(errorMessage)
                .build();
    }

    // String 타입의 errorMessage 를 받아서 ErrorResponse 객체를 생성하는 메소드
    public static ErrorResponse of(int httpStatusCode, int ncloudErrorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
                .httpStatusCode(httpStatusCode)
                .ncloudErrorCode(ncloudErrorCode)
                .errorMessage(createErrorMessage(bindingResult))
                .build();
    }

    // BindingResult 객체를 받아서 errorMessage 를 생성하는 메소드
    private static String createErrorMessage(BindingResult bindingResult) {
        boolean isFirst = true;

        StringBuilder stringBuilder = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            if (!isFirst) {
                stringBuilder.append(",");
            } else {
                isFirst = false;
            }

            stringBuilder.append("[");
            stringBuilder.append(fieldError.getField());
            stringBuilder.append("]");
            stringBuilder.append(fieldError.getDefaultMessage());
        }

        return stringBuilder.toString();
    }
}
