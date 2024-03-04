package com.mini.advice_park.domain.Comment.dto;

import com.mini.advice_park.domain.Comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {

    private final Long commentId;
    private final Long userId;
    private final Long postId;
    private final String content;

    @Builder
    public CommentResponse(Long commentId, Long userId, Long postId, String content) {
        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }

    public static CommentResponse from(Comment comment) {

        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .userId(comment.getUser().getUserId())
                .postId(comment.getPost().getPostId())
                .content(comment.getContent())
                .build();

    }

}
