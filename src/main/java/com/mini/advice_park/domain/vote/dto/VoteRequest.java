package com.mini.advice_park.domain.vote.dto;

import com.mini.advice_park.domain.post.entity.VoteOption;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteRequest {

    private VoteOption voteOption;

}
