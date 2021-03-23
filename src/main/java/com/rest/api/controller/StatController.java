package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.entity.fastcafe_stat.dto.IStatVanPayDailyGroupDTO;
import com.rest.api.entity.fastcafe_stat.dto.StatVanPayMonthlyTotalStatDTO;
import com.rest.api.entity.fastcafe_stat.dto.StatVanPayWeeklyTotalStatDTO;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.AdminService;
import com.rest.api.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class StatController {

    private final AdminService adminService;
    private final StatService statService;

    @GetMapping("/stat/daily")
    public CommonResult daily(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        Date maxDate = statService.getMaxIndexRegdateForStatVanPayDailyByBranchId(admin.getBranchId());

        if(maxDate == null) return DataResult.Success("lastdate", null).addResult("stats", null);

        LocalDate enddate = maxDate.toLocalDate();
        LocalDate startdate = enddate.plusDays(-6);
        List<IStatVanPayDailyGroupDTO> stats = statService.listStatVanPayDailyGroupByBranchId(admin.getBranchId(), Date.valueOf(startdate), Date.valueOf(enddate));

        return DataResult.Success("lastdate", maxDate).addResult("stats", stats);
    }

    @GetMapping("/stat/period")
    public CommonResult period(
            @RequestParam(defaultValue = "weekly") String period
            , @RequestParam(defaultValue = "") String year
            , @RequestParam(defaultValue = "") String month
            , @RequestParam(defaultValue = "") String machineType
            , @RequestParam(defaultValue = "0") Integer branch_machine_id
            ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        if(!StringUtils.hasText(year) || !StringUtils.hasText(month)){
            Date maxDate = statService.getMaxIndexRegdateForStatVanPayDailyByBranchId(admin.getBranchId());
            if(maxDate == null) return DataResult.Success("stats", null);

            LocalDate basedate = maxDate.toLocalDate();
            year = basedate.format(DateTimeFormatter.ofPattern("yyyy"));
            month = basedate.format(DateTimeFormatter.ofPattern("MM"));
        }

        if(!period.equalsIgnoreCase("weekly") && !period.equalsIgnoreCase("monthly"))
            return CommonResult.Fail(400, "잘못된 접근입니다.");

        if(period.equalsIgnoreCase("weekly")){
            // 주간
            List<StatVanPayWeeklyTotalStatDTO> stats = statService.listStatVanPayWeeklyGroupByBranchId(admin.getBranchId(), year, month, machineType, branch_machine_id);
            Date lastdate = statService.getMaxIndexRegdateForStatVanPayWeekly(admin.getBranchId(), year, month);
            return DataResult.Success("stats", stats).addResult("lastdate", lastdate);
        } else {
            // 월간
            List<StatVanPayMonthlyTotalStatDTO> stats = statService.listStatVanPayMonthlyGroupByBranchId(admin.getBranchId(), year, month, machineType, branch_machine_id);
            return DataResult.Success("stats", stats);
        }
    }
}
