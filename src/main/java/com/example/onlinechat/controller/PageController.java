package com.example.onlinechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 映射访问路径为 /login
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 映射访问路径为 /register
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}