package com.simplekey.spk_sai_sfmi.api.sfmi.mpc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "twb_bas03", schema = "f1_conf")
public class Twb_bas03 {

    @EmbeddedId
    private Twb_bas03Id id;

    @Column(name = "cd_nm")
    private String cdNm;

    @Column(name = "cd_nm_e")
    private String cdNmE;

    @Column(name = "cd_use_fr_dt")
    private String cdUseFrDt;

    @Column(name = "cd_use_to_dt")
    private String cdUseToDt;

    @Column(name = "cd_pre_type")
    private String cdPreType;

    @Column(name = "etc_info01")
    private String etcInfo01;

    @Column(name = "etc_info02")
    private String etcInfo02;

    @Column(name = "etc_info03")
    private String etcInfo03;

    @Column(name = "use_yn")
    private String useYn = "Y";  // Default value 'Y'

    @Column(name = "sort_ord")
    private Integer sortOrd = 0; // Default value 0

    @Column(name = "regr_dept_cd")
    private String regrDeptCd;

    @Column(name = "regr_id")
    private String regrId;

    @Column(name = "reg_dttm")
    private Timestamp regDttm;

    @Column(name = "amdr_dept_cd")
    private String amdrDeptCd;

    @Column(name = "amdr_id")
    private String amdrId;

    @Column(name = "upd_dttm")
    private Timestamp updDttm;

    @Column(name = "proc_id")
    private String procId;

    @Column(name = "it_processing")
    private Timestamp itProcessing;
}