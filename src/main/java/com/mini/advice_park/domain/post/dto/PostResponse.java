package com.mini.advice_park.domain.post.dto;

import com.mini.advice_park.domain.post.entity.Category;
import com.mini.advice_park.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class PostResponse {

    private final Long postId;
    private final Long userId;

    private final String title;
    private final String contents;

    private final Category category;
    private final boolean isVotingEnabled;

    private final List<String> imageUrls;

    @Builder
    public PostResponse(Long postId,
                        Long userId,
                        String title,
                        String contents,
                        Category category,
                        boolean isVotingEnabled,
                        List<String> imageUrls) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.isVotingEnabled = isVotingEnabled;
        this.imageUrls = imageUrls;
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
                .isVotingEnabled(post.isVotingEnabled())
                .imageUrls(imageUrls)
                .build();
    }
}
