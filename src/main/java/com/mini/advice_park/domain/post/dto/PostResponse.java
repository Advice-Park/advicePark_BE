package com.mini.advice_park.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mini.advice_park.domain.post.entity.Category;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.post.entity.PostVoteOption;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public class PostResponse {

    private final Long postId;
    private final Long userId;

    private final String title;
    private final String contents;

    private final Category category;
    private final PostVoteOption postVoteOption;

    private final long viewCount;
    private final long favoriteCount;
    private final long commentCount;

    private LocalDateTime createdTime;

    private final List<String> imageUrls;

    @Builder
    public PostResponse(Long postId,
                        Long userId,
                        String title,
                        String contents,
                        Category category,
                        PostVoteOption postVoteOption,
                        long viewCount,
                        long favoriteCount,
                        long commentCount,
                        LocalDateTime createdTime,
                        List<String> imageUrls) {

        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.postVoteOption = postVoteOption;
        this.viewCount = viewCount;
        this.favoriteCount = favoriteCount;
        this.commentCount = commentCount;
        this.createdTime = createdTime;
        this.imageUrls = imageUrls != null ? imageUrls : Collections.emptyList(); // 이미지 URL 리스트가 null일 경우 빈 리스트로 초기화

    }

    public static PostResponse from(Post post) {
        List<String> imageUrls = post.getImages()
                .stream()
                .filter(Objects::nonNull) // null 체크
                .map(image -> image.getStoredImagePath())
                .toList();

        return PostResponse.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .title(post.getTitle())
                .contents(post.getContents())
                .category(post.getCategory())
                .postVoteOption(post.getPostVoteOption())
                .viewCount(post.getViewCount())
                .favoriteCount(post.getFavoriteCount())
                .commentCount(post.getCommentCount())
                .createdTime(post.getCreatedTime())
                .imageUrls(imageUrls)
                .build();
    }

}
