package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.jwt.JwtTokenProvider;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class LoginController {

    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public CommonResult login(
            @RequestParam String account,
            @RequestParam String password
    ){
        Admin admin = adminService.fintByAccount(account).orElseThrow(AdminNotFoundException::new);
        if(!passwordEncoder.matches(password, admin.getPassword())){
            throw new AdminNotFoundException();
        }
        return DataResult.Success("accessToken", jwtTokenProvider.createToken(String.valueOf(admin.getId()), admin.getRole()));
    }
}
