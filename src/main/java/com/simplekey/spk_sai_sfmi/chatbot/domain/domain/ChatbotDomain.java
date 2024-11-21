package com.simplekey.spk_sai_sfmi.chatbot.domain.domain;

import com.simplekey.spk_sai_sfmi.chatbot.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatbotDomain extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatbotName;
    private String domainId;
    private String signature;
    private String secretKey;
    private String apiKey;
    private String type;    // clova, danbee

    @Builder
    public ChatbotDomain(String chatbotName, String domainId, String signature, String secretKey, String apiKey, String type) {
        this.chatbotName = chatbotName;
        this.domainId = domainId;
        this.signature = signature;
        this.secretKey = secretKey;
        this.apiKey = apiKey;
        this.type = type;
    }
}
