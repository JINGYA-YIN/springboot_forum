package com.example.onlinechat.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int id;
    private String title;
    private String content;
    private User sender;
    private LocalDateTime datetime;
    private final List<Reply> replies = new ArrayList<>();

    public synchronized void addReply(Reply reply) {
        replies.add(reply); //
    }

    public List<Reply> getReplies() {
        return Collections.unmodifiableList(replies); //
    }

    public List<Reply> getRootReplies() {
        return replies.stream()
                .filter(r -> r.getParentReplyID() == null) //
                .sorted(Comparator.comparing(Reply::getDatetime))
                .collect(Collectors.toList());
    }

    public synchronized boolean removeReply(int replyID) {
        // 1. 先找到要删除的目标对象
        Reply foundTarget = null;
        for (Reply r : replies) {
            if (r.getId() == replyID) {
                foundTarget = r;
                break;
            }
        }

        if (foundTarget == null) return false;

        final Reply target = foundTarget;

        // 2. 递归删除所有子回复
        List<Reply> children = new ArrayList<>(target.getChildReplies());
        for (Reply child : children) {
            removeReply(child.getId());
        }

        // 3. 从其父回复的列表中移除自己
        if (target.getParentReplyID() != null) {
            final int parentIdToSearch = target.getParentReplyID();

            replies.stream()
                    .filter(r -> r.getId() == parentIdToSearch)
                    .findFirst()
                    .ifPresent(parent -> parent.getChildReplies().remove(target));
        }

        // 4. 从帖子的总列表中移除
        return replies.remove(target);
    }
}