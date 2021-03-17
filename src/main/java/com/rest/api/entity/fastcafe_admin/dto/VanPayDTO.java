package com.rest.api.entity.fastcafe_admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rest.api.entity.fastcafe_admin.VanPay;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class VanPayDTO {
    private String machineType;
    private String machineName;
    private String deviceNo;
    private int payMoney;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp paydate;

    public VanPayDTO(VanPay vanPay){
        this.machineType = vanPay.getBranchMachine() != null ? vanPay.getBranchMachine().getMachineType() : null;
        this.machineName = vanPay.getBranchMachine() != null ? vanPay.getBranchMachine().getMachineName() : null;
        this.deviceNo = vanPay.getBranchMachine() != null ? vanPay.getBranchMachine().getDeviceNo() : null;
        this.payMoney = vanPay.getPayMoney();
        this.paydate = vanPay.getPaydate();
    }
}
