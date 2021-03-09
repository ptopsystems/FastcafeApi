package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.entity.fastcafe_admin.TotalPayDaily;
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

    @GetMapping("/pay/total")
    public CommonResult total(
            @RequestParam Date startdate,
            @RequestParam Date enddate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        Page<TotalPayDaily> totalPayDailies = payService.totalPayDailes(admin.getBranchId(), startdate, enddate, page, size);
        return DataResult.Success("total", totalPayDailies);
    }

}
