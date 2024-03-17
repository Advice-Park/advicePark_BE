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

    @Builder
    public Post(String title,
                String contents,
                Category category,
                VoteOption voteOption,
                User user,
                long viewCount) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.voteOption = voteOption;
        this.user = user;
        this.viewCount = viewCount;
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

    public void updatePostDetails(String title, String contents, Category category, VoteOption voteOption) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.voteOption = voteOption;
    }

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void clearImages() {
        this.images.clear();
    }

    // 조회수 증가 메서드
    public void increaseViewCount() {
        this.viewCount++;
    }

    // 댓글 수를 증가시키는 메서드
    public void increaseCommentCount() {
        this.commentCount++;
    }

    // 댓글 수를 감소시키는 메서드
    public void decreaseCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

}
