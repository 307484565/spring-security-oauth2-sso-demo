package com.zt.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @GetMapping("/user")
    public Authentication user(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/user/list")
    @PreAuthorize("hasAuthority('user:list')")
    public String normal() {
        return "这是需要user:list权限能访问的数据";
    }

    @GetMapping("/user/update")
    @PreAuthorize("hasAuthority('user:update')")
    public String admin() {
        return "这是需要user:update权限才可以访问的数据，访问会报403";
    }





}
