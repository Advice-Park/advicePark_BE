package com.mini.advice_park.domain.post.dto;

import com.mini.advice_park.domain.post.entity.Category;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.post.entity.VoteOption;
import lombok.Builder;
import lombok.Getter;

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
    private final VoteOption voteOption;

    private final long viewCount;
    private final long commentCount;

    private final List<String> imageUrls;

    @Builder
    public PostResponse(Long postId,
                        Long userId,
                        String title,
                        String contents,
                        Category category,
                        VoteOption voteOption,
                        long viewCount,
                        long commentCount, // 댓글 수 추가 필드
                        List<String> imageUrls) {

        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.voteOption = voteOption;
        this.viewCount = viewCount;
        this.commentCount = commentCount; // 댓글 수 필드 초기화
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
                .voteOption(post.getVoteOption())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount()) // 댓글 수 추가
                .imageUrls(imageUrls)
                .build();
    }

}
