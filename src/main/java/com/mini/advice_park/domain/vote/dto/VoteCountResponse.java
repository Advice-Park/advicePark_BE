package com.mini.advice_park.domain.vote.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteCountResponse {

    private int supportCount;
    private int opposeCount;

    @Builder
    public VoteCountResponse(int supportCount, int opposeCount) {
        this.supportCount = supportCount;
        this.opposeCount = opposeCount;
    }

    public static VoteCountResponse from(int supportCount, int opposeCount) {
        return VoteCountResponse.builder()
                .supportCount(supportCount)
                .opposeCount(opposeCount)
                .build();
    }

}
