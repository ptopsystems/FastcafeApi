package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.AdminService;
import com.rest.api.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class AdminController {

    private final AdminService adminService;
    private final BranchService branchService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/admin")
    public CommonResult admin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        return DataResult.Success("admin", admin);
    }

    @PatchMapping("/admin/info")
    public CommonResult update(
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String tel
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);
        if(!StringUtils.hasText(email) || !StringUtils.hasText(tel)){
            return CommonResult.Fail(500, "정보를 모두 입력해 주세요.");
        }
        admin = admin.withEmail(email)
                .withTel(tel);
        admin = adminService.save(admin);
        if(admin == null){
            return CommonResult.Fail(500, "변경중에 오류가 발생했습니다.");
        } else {
            return DataResult.Success("admin", admin);
        }
    }

    @PatchMapping("/admin/password")
    public CommonResult updatePassword(
            @RequestParam(defaultValue = "") String pw,
            @RequestParam(defaultValue = "") String newPw,
            @RequestParam(defaultValue = "") String confirmPw
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);
        if(!StringUtils.hasText(pw) || !StringUtils.hasText(newPw) || !StringUtils.hasText(confirmPw)){
            return CommonResult.Fail(500, "필드를 모두 입력해주세요.");
        } else if(!passwordEncoder.matches(pw, admin.getPassword())) {
            return CommonResult.Fail(500, "현재 암호가 올바르지 않습니다.");
        } else if(passwordEncoder.matches(newPw, admin.getPassword())) {
            return CommonResult.Fail(500, "동일한 암호로 변경할 수 없습니다.");
        } else if(!newPw.equals(confirmPw)){
            return CommonResult.Fail(500, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        } else {
            admin = adminService.save(admin.withPassword(passwordEncoder.encode(newPw)));
            if(admin == null){
                return CommonResult.Fail(500, "변경중에 오류가 발생했습니다.");
            }
            return CommonResult.Success();
        }
    }

    @PatchMapping("/admin/push")
    public CommonResult push(
            @RequestParam(defaultValue = "true") Boolean isPush
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);
        admin = adminService.save(admin.withIsPush(isPush));
        if(admin == null){
            return CommonResult.Fail(500, "변경중에 오류가 발생했습니다.");
        }
        return CommonResult.Success();
    }
}
