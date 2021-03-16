package com.rest.api.entity.fastcafe_stat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stat_van_pay_weekly")
public class StatVanPayWeekly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "branch_id")
    private int branchId;
    @Column(name = "branch_machine_id")
    private int branchMachineId;
    private String baseYear;
    private String baseMonth;
    private String baseWeek;
    private Date startdate;
    private Date enddate;
    private int payMoney;
    private int payCnt;
}
