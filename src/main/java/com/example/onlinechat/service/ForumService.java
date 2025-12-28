package com.example.onlinechat.service;

import com.example.onlinechat.model.Message;
import com.example.onlinechat.model.Reply;
import com.example.onlinechat.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ForumService {

    // 使用 ConcurrentHashMap 确保线程安全，无需手动加 synchronized
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final Map<Integer, Message> messages = new ConcurrentHashMap<>();

    // 使用 AtomicInteger 保证多线程环境下 ID 生成的原子性
    private final AtomicInteger nextUserID = new AtomicInteger(1);
    private final AtomicInteger nextMessageID = new AtomicInteger(1);
    private final AtomicInteger nextReplyID = new AtomicInteger(1);

    // --- 用户业务 ---

    public User register(String username, String password) {
        // 使用 Stream API 检查用户名唯一性
        boolean exists = users.values().stream()
                .anyMatch(u -> u.getUsername().equals(username));

        if (exists) return null;

        User user = new User(nextUserID.getAndIncrement(), username, password);
        users.put(user.getId(), user);
        return user;
    }

    public User login(String username, String password) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    // --- 发帖业务 ---

    public Message createMessage(String title, String content, User sender) {
        Message msg = new Message(nextMessageID.getAndIncrement(), title, content, sender, LocalDateTime.now());
        messages.put(msg.getId(), msg);
        return msg;
    }

    public Message getMessageByID(int messageID) {
        return messages.get(messageID);
    }

    public boolean removeMessage(int messageID, User requestUser) {
        Message msg = messages.get(messageID);
        // 验证作者权限后删除
        if (msg != null && msg.getSender().getId() == requestUser.getId()) {
            return messages.remove(messageID) != null;
        }
        return false;
    }

    // --- 回复业务 ---

    public Reply addReply(int messageID, Integer parentReplyID, String content, User sender) {
        Message msg = messages.get(messageID);
        if (msg == null) return null;

        Reply reply = new Reply(messageID, parentReplyID, content, sender, LocalDateTime.now());


        if (parentReplyID == null) {
            msg.addReply(reply);
        } else {
            // 在 Message 的总回复列表中查找父回复并添加子节点
            msg.getReplies().stream()
                    .filter(r -> r.getId() == parentReplyID)
                    .findFirst()
                    .ifPresent(parent -> parent.addChildReply(reply));

            msg.addReply(reply);
        }
        return reply;
    }

    public boolean removeReply(int messageID, int replyID, User requestUser) {
        Message msg = messages.get(messageID);
        if (msg == null) return false;

        // 验证该回复是否存在且属于当前用户
        boolean authorized = msg.getReplies().stream()
                .anyMatch(r -> r.getId() == replyID && r.getSender().getId() == requestUser.getId());

        if (authorized) {
            return msg.removeReply(replyID); // 调用 Message 内部的递归删除逻辑
        }
        return false;
    }

    // --- 分页与统计 ---

    public List<Message> getMessagesPage(int page, int pageSize) {
        return messages.values().stream()
                .sorted(Comparator.comparing(Message::getDatetime).reversed()) // 最新帖子在前
                .skip((long) (page - 1) * pageSize) // 跳过前面的项
                .limit(pageSize) // 限制取出的数量
                .collect(Collectors.toList());
    }

    public int getTotalPages(int pageSize) {
        int total = messages.size();
        return (int) Math.ceil((double) total / pageSize);
    }
}