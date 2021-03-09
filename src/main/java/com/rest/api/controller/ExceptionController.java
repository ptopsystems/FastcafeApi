package com.rest.api.controller;

import com.rest.api.exception.AuthenticationEntryPointException;
import com.rest.api.exception.JwtExpiredException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping(value = "/accessdenied")
    public void accessdenied(){
        throw new AccessDeniedException("");
    }

    @GetMapping(value = "/expired")
    public void expried(){
        throw new JwtExpiredException();
    }

    @GetMapping(value = "/entrypoint")
    public void entrypoint(){
        throw new AuthenticationEntryPointException();
    }
}
