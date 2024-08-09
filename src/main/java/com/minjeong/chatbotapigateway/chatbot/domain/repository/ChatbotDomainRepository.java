package com.minjeong.chatbotapigateway.chatbot.domain.repository;

import com.minjeong.chatbotapigateway.chatbot.domain.domain.ChatbotDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotDomainRepository extends JpaRepository<ChatbotDomain, Long> {
    ChatbotDomain findByDomainId(String domainId);
}
