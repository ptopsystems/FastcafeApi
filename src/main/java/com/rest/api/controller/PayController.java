package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.entity.fastcafe_admin.VanPay;
import com.rest.api.entity.fastcafe_admin.dto.VanPayDTO;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.AdminService;
import com.rest.api.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PayController {

    private final AdminService adminService;
    private final PayService payService;

    @GetMapping("/pay")
    public CommonResult pay(
            @RequestParam(required = false) Date startdate
            , @RequestParam(required = false) Date enddate
            , @RequestParam(defaultValue = "") String machineType
            , @RequestParam(defaultValue = "0") Integer branch_machine_id
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int size
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);
        if(startdate == null || enddate == null){
            Date maxDate = payService.getMaxIndexRegdate(admin.getBranchId());
            if(maxDate == null) return DataResult.Success("result", null);

            enddate = maxDate;
            startdate = Date.valueOf(maxDate.toLocalDate().minusDays(6));
        }

        Page<VanPay> vanPays = payService.listVanPay(admin.getBranchId(), startdate, enddate, machineType, branch_machine_id, page, size);
        Long totalPayMoney = payService.getSumPayMoney(admin.getBranchId(), startdate, enddate, machineType, branch_machine_id);
        return DataResult.Success("pays", vanPays.getContent().stream().map(VanPayDTO::new))
                .addResult("page", page)
                .addResult("size", size)
                .addResult("totalPages", vanPays.getTotalPages())
                .addResult("totalPayMoney", totalPayMoney);
    }
}
