package com.rest.api.entity.fastcafe_stat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stat_van_pay_monthly")
public class StatVanPayMonthly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "branch_id")
    private int branchId;
    @Column(name = "branch_machine_id")
    private int branchMachineId;
    private String baseYear;
    private String baseMonth;
    private double payMoney;
    private int payCnt;
}
