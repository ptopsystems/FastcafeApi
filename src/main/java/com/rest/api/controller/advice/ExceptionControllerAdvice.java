package com.rest.api.controller.advice;

import com.rest.api.exception.*;
import com.rest.api.result.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public CommonResult excption(HttpServletRequest req, HttpServletResponse res, final Exception e){
        e.printStackTrace();
        return CommonResult.Fail(500, e.getMessage());
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public CommonResult adminNotFoundException(HttpServletRequest req, HttpServletResponse res, final AdminNotFoundException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "잘못된 아이디 혹은 암호입니다.");
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public CommonResult branchNotFoundException(HttpServletRequest req, HttpServletResponse res, final BranchNotFoundException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "존재하지 않는 지점입니다.");
    }
    
    @ExceptionHandler(NoticeNotFoundException.class)
    public CommonResult noticeNotFoundException(HttpServletRequest req, HttpServletResponse res, final NoticeNotFoundException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "존재하지 않는 게시물입니다.");
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public CommonResult noticeNotFoundException(HttpServletRequest req, HttpServletResponse res, final BoardNotFoundException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "존재하지 않는 게시물입니다.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult accessDeniedException(HttpServletRequest req, HttpServletResponse res, final AccessDeniedException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "권한이 없습니다.");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    public CommonResult authenticationEntryPointException(HttpServletRequest req, HttpServletResponse res, final AuthenticationEntryPointException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "권한이 없습니다.");
    }

    @ExceptionHandler(JwtExpiredException.class)
    public CommonResult jwtExpiredException(HttpServletRequest req, HttpServletResponse res, final JwtExpiredException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "토큰이 만료되었습니다.");
    }

    @ExceptionHandler(MultipartFileConvertException.class)
    public CommonResult multipartFileConvertException(HttpServletRequest req, HttpServletResponse res, final MultipartFileConvertException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "업로드시 오류가 발생되었습니다.");
    }

    @ExceptionHandler(FileNotFoundException.class)
    public CommonResult multipartFileNotFoundException(HttpServletRequest req, HttpServletResponse res, final FileNotFoundException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "파일을 찾을 수 없습니다.");
    }

    @ExceptionHandler(MultipartFileNotMathcedTypeException.class)
    public CommonResult multipartFileNotMathcedTypeException(HttpServletRequest req, HttpServletResponse res, final MultipartFileNotMathcedTypeException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "업로드 할 수 없는 파일 형식입니다.");
    }

    @ExceptionHandler(MultipartFileNotAllowedTypeException.class)
    public CommonResult multipartFileNotAllowedTypeException(HttpServletRequest req, HttpServletResponse res, final MultipartFileNotAllowedTypeException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "업로드 할 수 없는 파일 형식입니다.");
    }

    @ExceptionHandler(MultipartFileNotAllowedFileNameException.class)
    public CommonResult multipartFileNotAllowedFileNameException(HttpServletRequest req, HttpServletResponse res, final MultipartFileNotAllowedFileNameException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "잘못된 파일명입니다.");
    }

    @ExceptionHandler(ManualNotFoundException.class)
    public CommonResult manualNotFoundException(HttpServletRequest req, HttpServletResponse res, final ManualNotFoundException e){
        e.printStackTrace();
        return CommonResult.Fail(500, "존재하지 않는 매뉴얼입니다.");
    }


}
