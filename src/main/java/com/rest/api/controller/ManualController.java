package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.entity.fastcafe_admin.BranchMachine;
import com.rest.api.entity.fastcafe_admin.Manual;
import com.rest.api.entity.fastcafe_admin.dto.ManualDTO;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.exception.ManualNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.AdminService;
import com.rest.api.service.BranchService;
import com.rest.api.service.ManualService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class ManualController {

    private final AdminService adminService;
    private final BranchService branchService;
    private final ManualService manualService;

    @GetMapping("/manual")
    public CommonResult manual(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        List<String> branchMachines = branchService.branchMachineFindByBranchId(admin.getBranchId())
                .stream()
                .map(BranchMachine::getMachineModel)
                .collect(Collectors.toList());

        List<Manual> manuals = manualService.list(branchMachines);
        return DataResult.Success("manuals", manuals.stream().map(ManualDTO::new));
    }


    @GetMapping("/manual/{id}")
    public CommonResult manualDetail(
            @PathVariable int id
    ){
        Manual manual = manualService.get(id).orElseThrow(ManualNotFoundException::new);
        return DataResult.Success("manual", manual);
    }

}
