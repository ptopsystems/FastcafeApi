package com.rest.api.entity.fastcafe_log;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Builder
@With
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "log_card_pay_by_api_data")
public class LogCardPayByApiData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "branch_id")
    private int branchId;
    private Date basedate;
    private Date startdate;
    private Date enddate;
    private int totalTran;
    private int approve;
    private int cancel;
    private int tranCnt;
    private int approveCnt;
    private int cancelCnt;
    private String result;
    private String errMsg;
    private String errDoc;
    private String eCode;
    private String eTrack;
    private int excuteTime;
    private Timestamp regdate;

}
