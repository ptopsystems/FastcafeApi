package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.entity.fastcafe_admin.Board;
import com.rest.api.entity.fastcafe_admin.BranchManageUrl;
import com.rest.api.entity.fastcafe_admin.Notice;
import com.rest.api.entity.fastcafe_admin.dto.BoardDTO;
import com.rest.api.entity.fastcafe_admin.dto.NoticeDTO;
import com.rest.api.entity.fastcafe_stat.StatDailyCardPayByApi;
import com.rest.api.entity.fastcafe_stat.StatMonthlyCardPayByApi;
import com.rest.api.entity.fastcafe_stat.dto.IStatVanPayDailyGroupDTO;
import com.rest.api.entity.fastcafe_stat.dto.IStatVanPayWeeklyGroupDTO;
import com.rest.api.entity.fastcafe_stat.dto.IStatWeeklyCardPayByApiDTO;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class DashBoardContoller {

    private final AdminService adminService;
    private final NoticeService noticeService;
    private final BoardService boardService;
    private final StatService statService;
    private final ManageService manageService;

    @GetMapping("/dashboard")
    public CommonResult dashboard(){
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

        // 서비스 바로가기
        List<BranchManageUrl> urls = manageService.list(admin.getBranchId());

        return DataResult.Success("month", month)
                .addResult("week", week)
                .addResult("day", day)
                .addResult("maxDate", maxDate)
                .addResult("notices", notices.getContent().stream().map(NoticeDTO::new))
                .addResult("boards", boards.getContent().stream().map(BoardDTO::new))
                .addResult("urls", urls.stream().map(BranchManageUrl::getManageUrl));
    }



    @GetMapping("/dashboard/paycard")
    public CommonResult dashboardPaycard(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        LocalDate yesterday = LocalDate.now().minusDays(1);

        StatMonthlyCardPayByApi month = statService.getOneStatMonthlyCardPayByApi(admin.getBranchId(), yesterday.format(DateTimeFormatter.ofPattern("yyyy")), yesterday.format(DateTimeFormatter.ofPattern("MM")));
        IStatWeeklyCardPayByApiDTO week = statService.getWeekSumStatDailyCardPayByApi(admin.getBranchId(), Date.valueOf(yesterday));
        StatDailyCardPayByApi day = statService.getStatDailyCardPayByApi(admin.getBranchId(), Date.valueOf(yesterday));

        //공지사항 3개
        Page<Notice> notices = noticeService.listWithPagable("",1, 3, admin.getId());

        // 문의 3개
        Page<Board> boards = boardService.listWithPagable(admin.getBranchId(), admin.getId(), null, null, 1, 3);

        // 서비스 바로가기
        List<BranchManageUrl> urls = manageService.list(admin.getBranchId());

        return DataResult.Success("month", month)
                .addResult("week", week)
                .addResult("day", day)
                .addResult("notices", notices.getContent().stream().map(NoticeDTO::new))
                .addResult("boards", boards.getContent().stream().map(BoardDTO::new))
                .addResult("urls", urls.stream().map(BranchManageUrl::getManageUrl));
    }
}
