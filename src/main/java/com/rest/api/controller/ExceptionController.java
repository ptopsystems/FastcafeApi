package com.rest.api.controller;

import com.rest.api.exception.AuthenticationEntryPointException;
import com.rest.api.exception.JwtExpiredException;
import com.rest.api.exception.JwtInvalidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/accessdenied")
    public void accessdenied(){
        throw new AccessDeniedException("");
    }

    @GetMapping("/expired")
    public void expried(){
        throw new JwtExpiredException();
    }

    @GetMapping("/entrypoint")
    public void entrypoint(){
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/invalid")
    public void invalid(){
        throw new JwtInvalidException();
    }
}
