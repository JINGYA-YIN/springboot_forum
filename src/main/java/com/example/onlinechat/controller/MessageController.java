package com.example.onlinechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.onlinechat.model.Message;
import com.example.onlinechat.model.User;
import com.example.onlinechat.service.ForumService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private ForumService forumService;

    // 帖子列表
    @GetMapping
    public String list(Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("messages", forumService.getMessagesPage(page, 10));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", forumService.getTotalPages(10));
        return "list";
    }

    // 帖子详情
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable int id, Model model) {
        Message message = forumService.getMessageByID(id);
        if (message == null) return "redirect:/messages";

        model.addAttribute("message", message);
        return "detail";
    }

    // 发帖
    @PostMapping("/create")
    public String create(@RequestParam String title, @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";

        forumService.createMessage(title, content, user);
        return "redirect:/messages";
    }

    // 删除帖子
    @PostMapping("/delete")
    public String delete(@RequestParam int messageID, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            forumService.removeMessage(messageID, user);
        }
        return "redirect:/messages";
    }

}

