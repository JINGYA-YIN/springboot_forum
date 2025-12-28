package com.example.onlinechat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    private int messageID;
    private Integer parentReplyID; // 父回复ID，允许为 null
    private String content;
    private User sender;
    private LocalDateTime datetime;

    // 用于存储子回复的列表，默认初始化为空的 ArrayList
    private final List<Reply> childReplies = new ArrayList<>();

    // 辅助方法：添加子回复
    public void addChildReply(Reply reply) {
        this.childReplies.add(reply);
    }

    public int getId() {
        return 0;
    }
}
