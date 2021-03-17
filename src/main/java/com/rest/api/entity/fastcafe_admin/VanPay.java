package com.rest.api.entity.fastcafe_admin;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "van_pay")
public class VanPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "branch_id")
    private int branchId;
    @Column(name ="branch_machine_id")
    private int branchMachineId;
    private Date indexRegdate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cardNum;
    private String approvalNo;
    private int payMoney;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp paydate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp regdate;

    @ManyToOne
    @JoinColumn(name = "branch_machine_id", insertable = false, updatable = false)
    private BranchMachine branchMachine;
}
