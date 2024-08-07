package com.mini.advice_park.domain.vote.entity;

import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "votes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private VoteOption voteOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Vote(User user, VoteOption voteOption, Post post) {
        this.user = user;
        this.voteOption = voteOption;
        this.post = post;
    }

    public static Vote of(User user, VoteOption voteOption, Post post) {
        return Vote.builder()
                .user(user)
                .voteOption(voteOption)
                .post(post)
                .build();
    }

    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }

}
