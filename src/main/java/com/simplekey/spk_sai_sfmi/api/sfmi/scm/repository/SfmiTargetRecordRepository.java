package com.simplekey.spk_sai_sfmi.api.sfmi.scm.repository;

import com.simplekey.spk_sai_sfmi.api.sfmi.scm.domain.SfmiTargetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SfmiTargetRecordRepository extends JpaRepository<SfmiTargetRecord, String> {
    SfmiTargetRecord findBySid(String sid);
}
