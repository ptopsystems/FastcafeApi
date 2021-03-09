package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.entity.fastcafe_admin.Branch;
import com.rest.api.entity.fastcafe_admin.BranchMachine;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.exception.BranchNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.AdminService;
import com.rest.api.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class BranchController {

    private final BranchService branchService;
    private final AdminService adminService;

    @GetMapping("/branch")
    public CommonResult branch(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);
        Branch branch = branchService.findById(admin.getBranchId()).orElseThrow(BranchNotFoundException::new);

        return DataResult.Success("branch", branch);
    }

    @GetMapping("/branch/machine")
    public CommonResult machines(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);
        List<BranchMachine> branchMachines = branchService.branchMachineFindByBranchId(admin.getBranchId());

        return DataResult.Success("machines", branchMachines);
    }
}
