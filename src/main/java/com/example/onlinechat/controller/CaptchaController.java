package com.example.onlinechat.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController // 自动处理 Body 响应
public class CaptchaController {

    @Autowired
    private ResourceLoader resourceLoader; // Spring 推荐的资源加载方式

    @GetMapping("/captcha")
    public void getCaptcha(HttpSession session, HttpServletResponse resp) throws IOException {
        String captcha = generateCaptcha(4);
        session.setAttribute("captcha", captcha);

        // 从 static 目录下读取背景图
        Resource resource = resourceLoader.getResource("classpath:static/image/img.png");
        BufferedImage bgImage = ImageIO.read(resource.getInputStream());

        Graphics2D g = bgImage.createGraphics();
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.setColor(Color.RED);
        g.drawString(captcha, 10, bgImage.getHeight() - 10);
        g.dispose();

        resp.setContentType("image/png");
        ImageIO.write(bgImage, "png", resp.getOutputStream());
    }

    private String generateCaptcha(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
}