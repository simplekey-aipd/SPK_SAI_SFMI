package com.minjeong.chatbotapigateway.chatflow.controller;

import com.minjeong.chatbotapigateway.chatflow.dto.request.MessageRequestDto;
import com.minjeong.chatbotapigateway.chatflow.dto.request.WelcomeRequestDto;
import com.minjeong.chatbotapigateway.chatflow.service.ChatflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chatflow/v1")
public class ChatflowController {

    private final ChatflowService chatflowService;

    @PostMapping("/welcome")
    public ResponseEntity<?> welcome(@RequestBody WelcomeRequestDto dto) {
        return ResponseEntity.ok(chatflowService.getWelcomeMessage(dto));
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequestDto dto) {
        return ResponseEntity.ok(chatflowService.sendMessage(dto));
    }

    // TODO : close, timeout API 추가

    /*
    ## chatflow API
    - welcome, message

    ------ 기본 정보 ------
    url :
    https://danbee.ai/chatflow/chatbot/{version}/{chatbotId}/welcome.do
    https://danbee.ai/chatflow/chatbot/{version}/{chatbotId}/message.do

    method : POST

    header : "Content-Type": "application/json"

    ---------------------

    # welcome message API

    - path parameter
    version, chatbotId

    - request body
    user_id

    - response
    (required=true)
    session_id, ins_id(대화 흐름 인스턴스), intent_id(대화 의도-slot, carousel)
    node_id, param_id(slot, carousel)


    # message flow API

    - path parameter

    - request body

    - response
    (required=true)

    */
}
