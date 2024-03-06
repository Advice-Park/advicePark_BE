package com.mini.advice_park.domain.Comment.dto;

import com.mini.advice_park.domain.Comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {

    // TODO 댓글에 닉네임 들어감?

    private Long commentId;
    private Long userId;
    private Long postId;
    private String content;
    private int likeCount;

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
