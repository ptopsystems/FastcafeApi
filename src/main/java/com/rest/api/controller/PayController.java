package com.rest.api.controller;

import com.amazonaws.util.StringUtils;
import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.entity.fastcafe_admin.CardPayByApi;
import com.rest.api.entity.fastcafe_admin.VanPay;
import com.rest.api.entity.fastcafe_admin.dto.CardPayByApiDTO;
import com.rest.api.entity.fastcafe_admin.dto.VanPayDTO;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.properties.BankCodeSource;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PayController {

    private final AdminService adminService;
    private final PayService payService;

    private final BankCodeSource bankCodeSource;

    @GetMapping("/_pay")
    public CommonResult pay(
            @RequestParam(required = false) String startdateStr
            , @RequestParam(required = false) String enddateStr
            , @RequestParam(defaultValue = "") String machineType
            , @RequestParam(defaultValue = "0") Integer branch_machine_id
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int size
    ){
        Date startdate = null;
        Date enddate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            if(!StringUtils.isNullOrEmpty(startdateStr)) {
                startdate = new Date( sdf.parse( startdateStr ).getTime() );
            }
            if(!StringUtils.isNullOrEmpty(enddateStr)) {
                enddate = new Date( sdf.parse( enddateStr ).getTime() );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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

    @GetMapping("/pay/bankcode")
    public CommonResult bankcode(){
        return DataResult.Success("banks", bankCodeSource.getBanks());
    }

    @GetMapping("/pay")
    public CommonResult cardPay(
            @RequestParam(name = "startdate", required = false) String strStartdate
            , @RequestParam(name = "enddate", required = false) String strEnddate
            , @RequestParam(name = "payType", defaultValue = "") String payType
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);
        //
        Date startdate = null;
        Date enddate = null;
        if(StringUtils.isNullOrEmpty(strStartdate) || StringUtils.isNullOrEmpty(strEnddate)){
            Date maxDate = payService.getMaxTransDateForCardPayByApi(admin.getBranchId());
            if(maxDate == null) return DataResult.Success("result", null);

            enddate = maxDate;
            startdate = Date.valueOf(maxDate.toLocalDate().minusDays(6));
        } else {
            startdate = Date.valueOf(LocalDate.parse(strStartdate, DateTimeFormatter.ofPattern("yyyy.MM.dd")));
            enddate = Date.valueOf(LocalDate.parse(strEnddate, DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        }

        Page<CardPayByApi> cardPayByApis = payService.listCardPayByApi(admin.getBranchId(), startdate, enddate, payType, page, size);
        return DataResult.Success("pays", cardPayByApis.getContent().stream().map(CardPayByApiDTO::new))
                .addResult("page", page)
                .addResult("size", size)
                .addResult("totalPages", cardPayByApis.getTotalPages());
    }
}
