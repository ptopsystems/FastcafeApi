package com.rest.api.controller;

import com.rest.api.service.AdminService;
import com.rest.api.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PayController {

    private final AdminService adminService;
    private final PayService payService;
}
