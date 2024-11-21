package com.simplekey.spk_sai_sfmi.chatbot.clova.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRequestDto {

    private String userId;
    @Setter
    private String text;
}
