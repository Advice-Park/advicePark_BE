package com.mini.advice_park.domain.favorite.dto;

import com.mini.advice_park.domain.favorite.entity.UserPostFavorite;
import lombok.Getter;

@Getter
public class UserPostFavoriteDto {

    private Long favoriteId;
    private Long userId;
    private Long postId;

    public UserPostFavoriteDto(Long favoriteId, Long userId, Long postId) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.postId = postId;
    }

    public static UserPostFavoriteDto fromEntity(UserPostFavorite userPostFavorite) {
        return new UserPostFavoriteDto(
                userPostFavorite.getFavoriteId(),
                userPostFavorite.getUser().getUserId(),
                userPostFavorite.getPost().getPostId()
        );
    }
}
