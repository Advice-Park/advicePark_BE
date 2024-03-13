package com.mini.advice_park.domain.post.dto;

import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostDetailResponse {

    /**
     * 게시글 조회시 댓글 정보 포함 반환
     */

    private final PostResponse postResponse;
    private final List<CommentResponse> comments;
    private final int commentCount; // 댓글 수 추가 필드

    @Builder
    public PostDetailResponse(PostResponse postResponse, List<CommentResponse> comments, int commentCount) {
        this.postResponse = postResponse;
        this.comments = comments;
        this.commentCount = commentCount; // 댓글 수 필드 초기화
    }
}
