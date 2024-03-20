package com.mini.advice_park.domain.post.entity;

import com.mini.advice_park.domain.Image.Image;
import com.mini.advice_park.domain.post.dto.PostRequest;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private VoteOption voteOption;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Image> images = new ArrayList<>();

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private long commentCount;

    @Column(nullable = false)
    private Long favoriteCount;

    @Builder
    public Post(String title,
                String contents,
                Category category,
                VoteOption voteOption,
                User user,
                long viewCount,
                long favoriteCount) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.voteOption = voteOption;
        this.user = user;
        this.viewCount = viewCount;
        this.favoriteCount = favoriteCount;
        this.commentCount = 0; // 댓글 수 초기화
    }

    public static Post of(PostRequest postRequest) {
        return Post.builder()
                .title(postRequest.getTitle())
                .contents(postRequest.getContents())
                .category(postRequest.getCategory())
                .voteOption(postRequest.getVoteOption())
                .build();
    }

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    /**
     * 조회수를 증가 시키는 메서드
     */
    public void increaseViewCount() {
        this.viewCount++;
    }

    /**
     * 댓글 수를 증가,감소 시키는 메서드
     */
    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    /**
     * 즐겨찾기 수를 증가,감소 시키는 메서드
     */
    public void increaseFavoriteCount() {
        this.favoriteCount++;
    }

    public void decreaseFavoriteCount() {
        if (this.favoriteCount > 0) {
            this.favoriteCount--;
        }
    }

}
