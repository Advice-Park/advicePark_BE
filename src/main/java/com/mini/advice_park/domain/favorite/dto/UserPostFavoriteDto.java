package com.mini.advice_park.domain.favorite.dto;

import com.mini.advice_park.domain.favorite.entity.UserPostFavorite;
import com.mini.advice_park.domain.post.dto.PostResponse;
import lombok.Getter;

@Getter
public class UserPostFavoriteDto {

    private Long favoriteId;
    private Long userId;
    private PostResponse post; // 포스트의 전체 내용을 담을 객체

    public UserPostFavoriteDto(Long favoriteId, Long userId, PostResponse post) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.post = post;
    }

    public static UserPostFavoriteDto from(UserPostFavorite userPostFavorite) {
        return new UserPostFavoriteDto(
                userPostFavorite.getFavoriteId(),
                userPostFavorite.getUser().getUserId(),
                PostResponse.from(userPostFavorite.getPost())
        );
    }

}