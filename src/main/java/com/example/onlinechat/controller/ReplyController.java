package com.example.onlinechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.onlinechat.service.ForumService;
import com.example.onlinechat.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reply")

public class ReplyController {
    @Autowired
    private ForumService forumService;

    @PostMapping("/add")
    public String add(@RequestParam int messageID,
                      @RequestParam(required = false) Integer parentReplyID,
                      @RequestParam String content,
                      HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";

        forumService.addReply(messageID, parentReplyID, content, user);
        return "redirect:/messages/detail/" + messageID;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int messageID, @RequestParam int replyID, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            forumService.removeReply(messageID, replyID, user);
        }
        return "redirect:/messages/detail/" + messageID;
    }
}

