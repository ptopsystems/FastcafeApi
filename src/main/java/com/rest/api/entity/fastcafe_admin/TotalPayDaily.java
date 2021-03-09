package com.rest.api.entity.fastcafe_admin;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "total_pay_daily")
public class TotalPayDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;

    @Column(name = "branch_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int branchId;
    @Column(name = "branch_machine_id")
    private int branch_machine_id;

    private Date indexRegdate;
    private int payMoney;
    private int payCnt;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp regdate;
}
