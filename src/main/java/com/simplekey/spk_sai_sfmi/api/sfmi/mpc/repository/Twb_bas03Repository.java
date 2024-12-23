package com.simplekey.spk_sai_sfmi.api.sfmi.mpc.repository;

import com.simplekey.spk_sai_sfmi.api.sfmi.mpc.domain.Twb_bas03;
import com.simplekey.spk_sai_sfmi.api.sfmi.mpc.domain.Twb_bas03Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Twb_bas03Repository extends JpaRepository<Twb_bas03, Twb_bas03Id> {
    @Query(value = "SELECT * FROM f1_conf.twb_bas03 WHERE cen_type_cd = ?1 AND group_cd = ?2 AND cd = ?3", nativeQuery = true)
    Optional<Twb_bas03> findByCenTypeCdAndGroupCdAndCd(String cenTypeCd, String groupCd, String cd);
}
