package com.simplekey.spk_sai_sfmi.api.sfmi.scm.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditingEntityReg {

    @CreatedBy
    @Column(name = "REG_BY", updatable = false)
    protected String regBy;

    //@CreatedDate
    @Column(name = "REG_TIME", updatable = false)
    protected String regTime;
    //private LocalDateTime regTime;

    @PrePersist
    protected void onInsert() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.regTime = date;
    }

}
