package com.mini.advice_park.domain.Comment.dto;

import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.Comment.entity.CommentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private Long commentId;
    private Long userId;
    private Long postId;
    private String content;
    private int likeCount;
    private LocalDateTime createdTime;
    private CommentType commentType;

    @Builder
    public CommentResponse(Long commentId,
                           Long userId,
                           Long postId,
                           String content,
                           int likeCount,
                           LocalDateTime createdTime,
                           CommentType commentType) {

        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.likeCount = likeCount;
        this.createdTime = createdTime;
        this.commentType = commentType;
    }

    public static CommentResponse from(Comment comment) {

        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .userId(comment.getUser().getUserId())
                .postId(comment.getPost().getPostId())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .createdTime(comment.getCreatedTime())
                .commentType(comment.getCommentType())
                .build();
    }

}
