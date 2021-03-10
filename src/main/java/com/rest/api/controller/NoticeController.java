package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.entity.fastcafe_admin.Notice;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.exception.NoticeNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.AdminService;
import com.rest.api.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class NoticeController {

    private final NoticeService noticeService;
    private final AdminService adminService;

    @GetMapping("/notice")
    private CommonResult notice(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        Page<Notice> notices = noticeService.findAll(page, size, admin.getId());
        if(notices.isEmpty()){
            return CommonResult.Fail(400, "데이터가 존재하지 않습니다.");
        }
        return DataResult.Success("notices", notices);
    }

    @GetMapping("/notice/{id}")
    private CommonResult noticeDetail(
            @PathVariable int id
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        Notice notice = noticeService.findById(id, admin.getId()).orElseThrow(NoticeNotFoundException::new);

        return DataResult.Success("notice", notice);
    }
}
