package com.example.onlinechat.controller;

import com.example.onlinechat.model.User;
import com.example.onlinechat.service.ForumService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")

public class UserController {
    @Autowired
    private ForumService forumService;

    // 跳转登录页
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 对应 templates/login.html
    }

    // 跳转注册页
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // 登录处理
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String captcha,
                              HttpSession session,
                              Model model) {
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            model.addAttribute("error", "验证码错误");
            return "login";
        }

        User user = forumService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(24 * 60 * 60);
            return "redirect:/messages";
        }
        model.addAttribute("error", "用户名或密码错误");
        return "login";
    }

    // 注册处理
    @PostMapping("/register")
    public String handleRegister(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String captcha,
                                 HttpSession session,
                                 Model model) {
        // 验证码逻辑同上...
        User user = forumService.register(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/messages";
        }
        model.addAttribute("error", "用户名已存在");
        return "register";
    }

    // 退出登录
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}

