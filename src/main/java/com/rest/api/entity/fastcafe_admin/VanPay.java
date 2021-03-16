package com.rest.api.entity.fastcafe_admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "van_pay")
public class VanPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int branch_id;
    private int branch_machine_id;
    private Date indexRegdate;
    private String cardNum;
    private String approvalNo;
    private int payMoney;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp paydate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp regdate;

}
