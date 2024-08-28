package com.minjeong.chatbotapigateway.chatbot.domain.domain;

import com.minjeong.chatbotapigateway.chatbot.global.BaseEntity;
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

    @Builder
    public ChatbotDomain(String chatbotName, String domainId, String signature, String secretKey) {
        this.chatbotName = chatbotName;
        this.domainId = domainId;
        this.signature = signature;
        this.secretKey = secretKey;
    }
}