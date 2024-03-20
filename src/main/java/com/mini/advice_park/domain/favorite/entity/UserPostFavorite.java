package com.mini.advice_park.domain.favorite.entity;

import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_post_favorites")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPostFavorite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public UserPostFavorite(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public static UserPostFavorite of(User user, Post post) {
        return UserPostFavorite.builder()
                .user(user)
                .post(post)
                .build();
    }

}
