package com.simplekey.spk_sai_sfmi.api.sfmi.scm.domain;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString
@Table(name = "t_tgt_record_ssf")
public class SfmiTargetRecord extends AuditingEntityReg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TGT_RECORD_SEQ")
    private String tgtRecordSeq;

    @Column(name = "SRC_RECORD_SEQ")
    private String srcRecordSeq;

    @Column(name = "SRC_LIST_ID")
    private String srcRecordListId;

    @Column(name = "TGT_LIST_ID")
    private String tgtListId;

    @Column(name = "TGT_LIST_NM")
    private String tgtListNm;

    @Column(name = "CAMPAIGN_NM")
    private String campaignNm;

    @Column(name = "CALLLIST_NM")
    private String calllistNm;

    @Column(name = "SID")
    private String sid;

    @Column(name = "BP_ADD_FLAG")
    private int bpAddFlag;

    @Column(name = "REQ_SN")
    private String reqSn;

    @Column(name = "CSRC_CD")
    private String csrcCd;

    @Column(name = "CAR_NO")
    private String carNo;

    @Column(name = "CONTACT_NO")
    private String contactNo;

    @Column(name = "CAR_NM")
    private String carNm;

    @Column(name = "TYPE_CD")
    private String typeCd;

    @Column(name = "TYPE_DETAIL_CD")
    private String typeDetailCd;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ADDRESS_DETAIL")
    private String addressDetail;

    @Column(name = "ADDRESS_GPS_LOT")
    private String addressGpsLot;

    @Column(name = "ADDRESS_GPS_LAT")
    private String addressGpsLat;

    @Column(name = "HIGHWAY_FLAG")
    private String highwayFlag;

    @Column(name = "TECH1_PRIORITY_NO")
    private String tech1PriorityNo;

    @Column(name = "TECH1_CONTACT_NO")
    private String tech1ContactNo;

    @Column(name = "TECH2_PRIORITY_NO")
    private String tech2PriorityNo;

    @Column(name = "TECH2_CONTACT_NO")
    private String tech2ContactNo;

    @Column(name = "TECH3_PRIORITY_NO")
    private String tech3PriorityNo;

    @Column(name = "TECH3_CONTACT_NO")
    private String tech3ContactNo;

    @Column(name = "RECORD_NO")
    private String recordNo;

    @Column(name = "CALLER_ID")
    private String callerId;

    @Column(name = "SCHEDULED_TIME")
    private String scheduledTime;

    @Column(name = "CALLED_STATE")
    private String calledState;

    @Column(name = "RECEIVED_TIME")
    private String receivedTime;

}