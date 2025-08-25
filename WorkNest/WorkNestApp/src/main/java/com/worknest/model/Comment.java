package com.worknest.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", columnDefinition = "TIMESTAMP")
    private Date timestamp;

    // getters and setters
    public Long getCommentId() { return commentId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }
    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
