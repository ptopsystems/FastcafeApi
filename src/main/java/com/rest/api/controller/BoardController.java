package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.entity.fastcafe_admin.Board;
import com.rest.api.entity.fastcafe_admin.dto.BoardDTO;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.exception.BoardNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.AdminService;
import com.rest.api.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class BoardController {

    private final BoardService boardService;
    private final AdminService adminService;

    /**
     * 전체 문의 내역 조회
     * @param stat
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/board")
    public CommonResult board(
        @RequestParam(defaultValue = "") String searchValue,
        @RequestParam(defaultValue = "") String stat,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        Page<Board> boards = boardService.listWithPagable(admin.getBranchId(), admin.getId(), searchValue, stat, page, size);

        return DataResult.Success("boards", boards.getContent().stream().map(BoardDTO::new))
                .addResult("totalPages", boards.getTotalPages())
                .addResult("page", page)
                .addResult("size", size);
    }

    /**
     * 상세 문의내역 조희
     * @param id
     * @return
     */
    @GetMapping("/board/{id}")
    public CommonResult boardDetail(
            @PathVariable int id
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        Board board = boardService.find(id, admin.getId()).orElseThrow(BoardNotFoundException::new);

        return DataResult.Success("board", board);
    }

    /**
     * 문의내역 생성
     * @param title
     * @param content
     * @param accidentTime
     * @param attachImgUrl
     * @return
     */
    @PostMapping("/board")
    public CommonResult boardInsert(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String accidentTime,
            @RequestParam(required = false) String attachImgUrl
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        if(!StringUtils.hasText(title) || !StringUtils.hasText(content) || !StringUtils.hasText(accidentTime)){
            return CommonResult.Fail(400, "필드를 모두 입력해 주세요.");
        }

        Board board = Board.builder()
                .branchId(admin.getBranchId())
                .adminId(admin.getId())
                .title(title)
                .content(content)
                .accidentTime(accidentTime)
                .attachImgUrl(attachImgUrl)
                .stat("1002")
                .build();
        board = boardService.save(board);

        if(board == null){
            return CommonResult.Fail(500, "저장 중 오류가 발생되었습니다.");
        }
        return CommonResult.Success(200, "저장되었습니다.");
    }

    /**
     * 문의내역 수정
     * @param id
     * @param title
     * @param content
     * @param accidentTime
     * @param attachImgUrl
     * @return
     */
    @PatchMapping("/board/{id}")
    public CommonResult boardUpdate(
            @PathVariable int id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String accidentTime,
            @RequestParam(required = false) String attachImgUrl
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        if(!StringUtils.hasText(title) || !StringUtils.hasText(content) || !StringUtils.hasText(accidentTime)){
            return CommonResult.Fail(400, "필드를 모두 입력해 주세요.");
        }

        Board board = boardService.find(id, admin.getId()).orElseThrow(BoardNotFoundException::new);
        if(!"1002".equals(board.getStat())){
            return CommonResult.Fail(500, "수정이 불가능합니다.");
        }
        board = board
                .withTitle(title)
                .withContent(content)
                .withAccidentTime(accidentTime)
                .withAttachImgUrl(attachImgUrl);

        board = boardService.save(board);
        if(board == null){
            return CommonResult.Fail(500, "수정 중 오류가 발생되었습니다.");
        }
        return CommonResult.Success(200, "수정되었습니다.");
    }

    /**
     * 문의내역 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/board/{id}")
    public CommonResult boardDelete(
            @PathVariable int id
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.fintByAccount(authentication.getName()).orElseThrow(AdminNotFoundException::new);

        Board board = boardService.find(id, admin.getId()).orElseThrow(BoardNotFoundException::new);
        if(!"1002".equals(board.getStat())){
            return CommonResult.Fail(500, "삭제가 불가능합니다.");
        }
        board = board
                .withStat("9001");

        board = boardService.save(board);
        if(board == null){
            return CommonResult.Fail(500, "수정 중 오류가 발생되었습니다.");
        }
        return CommonResult.Success(200, "삭제되었습니다.");
    }
}
