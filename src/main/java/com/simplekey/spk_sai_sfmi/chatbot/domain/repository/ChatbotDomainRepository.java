package com.simplekey.spk_sai_sfmi.chatbot.domain.repository;

import com.simplekey.spk_sai_sfmi.chatbot.domain.domain.ChatbotDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotDomainRepository extends JpaRepository<ChatbotDomain, Long> {
    ChatbotDomain findByDomainId(String domainId);
}
