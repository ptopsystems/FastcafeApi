package com.rest.api.entity.fastcafe_admin;

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
@Table(name = "card_pay_by_api")
public class CardPayByApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "branch_id")
    private int branchId;
    private Date transDate;
    private String transTime;
    private String authClassName;
    private String cardNm;
    private String cardNo;
    private String appNo;
    private int appAmt;
    private String instrmNm;
    private Timestamp regdate;
}
