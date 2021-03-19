package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.entity.fastcafe_admin.Board;
import com.rest.api.entity.fastcafe_admin.Notice;
import com.rest.api.entity.fastcafe_admin.dto.BoardDTO;
import com.rest.api.entity.fastcafe_admin.dto.NoticeDTO;
import com.rest.api.entity.fastcafe_stat.dto.IStatVanPayDailyGroupDTO;
import com.rest.api.entity.fastcafe_stat.dto.IStatVanPayWeeklyGroupDTO;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.AdminService;
import com.rest.api.service.BoardService;
import com.rest.api.service.NoticeService;
import com.rest.api.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class DashBoardContoller {

    private final AdminService adminService;
    private final NoticeService noticeService;
    private final BoardService boardService;
    private final StatService statService;

    @GetMapping("/dashboard")
    public CommonResult branch(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        // 결제금액/결제건
        Date maxDate = statService.getMaxIndexRegdateForStatVanPayDailyByBranchId(admin.getId());
        IStatVanPayDailyGroupDTO month = null;
        IStatVanPayWeeklyGroupDTO week = null;
        IStatVanPayDailyGroupDTO day = null;
        if(maxDate != null){
            LocalDate enddate = maxDate.toLocalDate();
            LocalDate startdate = enddate.with(TemporalAdjusters.firstDayOfMonth());

            month = statService.getStatVanPayDailyGroupByBranchId(admin.getBranchId(), Date.valueOf(startdate), Date.valueOf(enddate));
            week = statService.getStatVanPayWeeklyGroupByBranchId(admin.getBranchId(), Date.valueOf(enddate));
            day = statService.getStatVanPayDaily(admin.getBranchId(), Date.valueOf(enddate));
        }

        //공지사항 3개
        Page<Notice> notices = noticeService.listWithPagable("",1, 3, admin.getId());

        // 문의 3개
        Page<Board> boards = boardService.listWithPagable(admin.getBranchId(), admin.getId(), null, null, 1, 3);


        return DataResult.Success("month", month)
                .addResult("week", week)
                .addResult("day", day)
                .addResult("maxDate", maxDate)
                .addResult("notices", notices.getContent().stream().map(NoticeDTO::new))
                .addResult("boards", boards.getContent().stream().map(BoardDTO::new));
    }
}
