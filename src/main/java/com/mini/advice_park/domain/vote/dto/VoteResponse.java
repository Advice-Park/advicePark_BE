package com.mini.advice_park.domain.vote.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteResponse {

    private Long voteId;
    private Long userId;
    private Long postId;
    private String voteOption;

    private int supportCount;
    private int opposeCount;

    @Builder
    public VoteResponse(Long voteId, Long userId, Long postId,
                        String voteOption, int supportCount, int opposeCount) {
        this.voteId = voteId;
        this.userId = userId;
        this.postId = postId;
        this.voteOption = voteOption;
        this.supportCount = supportCount;
        this.opposeCount = opposeCount;
    }

    public static VoteResponse from(Long voteId, Long userId, Long postId,
                                    String voteOption, int supportCount, int opposeCount) {
        return VoteResponse.builder()
                .voteId(voteId)
                .userId(userId)
                .postId(postId)
                .voteOption(voteOption)
                .opposeCount(opposeCount)
                .supportCount(supportCount)
                .build();
    }

}
