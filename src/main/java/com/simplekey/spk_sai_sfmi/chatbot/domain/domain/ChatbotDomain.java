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
@Table(name = "chatbot_domain")
public class ChatbotDomain extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatbot_name")
    private String chatbotName;

    @Column(name = "domain_id")
    private String domainId;

    @Column(name = "signature")
    private String signature;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "type")
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
