package com.rest.api.controller.advice;

import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.exception.AuthenticationEntryPointException;
import com.rest.api.exception.BranchNotFoundException;
import com.rest.api.exception.JwtExpiredException;
import com.rest.api.result.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(AdminNotFoundException.class)
    public CommonResult adminNotFoundException(HttpServletRequest req, HttpServletResponse res, final AdminNotFoundException e){
        return CommonResult.Fail(500, "잘못된 아이디 혹은 암호입니다.");
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public CommonResult branchNotFoundException(HttpServletRequest req, HttpServletResponse res, final BranchNotFoundException e){
        return CommonResult.Fail(500, "존재하지 않는 지점입니다.");
    }

    @ExceptionHandler({AccessDeniedException.class, AuthenticationEntryPointException.class})
    public CommonResult accessDeniedException(HttpServletRequest req, HttpServletResponse res, final AccessDeniedException e){
        return CommonResult.Fail(500, "권한이 없습니다.");
    }

    @ExceptionHandler(JwtExpiredException.class)
    public CommonResult jwtExpiredException(HttpServletRequest req, HttpServletResponse res, final JwtExpiredException e){
        return CommonResult.Fail(500, "토큰이 만료되었습니다.");
    }
}
