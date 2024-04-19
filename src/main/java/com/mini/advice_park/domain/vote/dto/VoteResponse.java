package com.mini.advice_park.domain.vote.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteResponse {

    private Long voteId;
    private Long userId;
    private Long postId;
    private String voteOption;

    @Builder
    public VoteResponse(Long voteId, Long userId, Long postId, String voteOption) {
        this.voteId = voteId;
        this.userId = userId;
        this.postId = postId;
        this.voteOption = voteOption;
    }

    public static VoteResponse from(Long voteId, Long userId, Long postId, String voteOption) {
        return VoteResponse.builder()
                .voteId(voteId)
                .userId(userId)
                .postId(postId)
                .voteOption(voteOption)
                .build();
    }

}
