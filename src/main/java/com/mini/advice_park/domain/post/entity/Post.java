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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Image> images = new ArrayList<>(); // 이미지 리스트를 비어있는 리스트로 초기화

    private boolean isVotingEnabled;

    @Column(nullable = false)
    private long viewCount; // 조회수를 나타내는 필드

    @Builder
    public Post(String title,
                String contents,
                Category category,
                User user,
                List<Image> images,
                boolean isVotingEnabled,
                long viewCount) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.user = user;
        if (images != null) {
            this.images.addAll(images);
        }
        this.isVotingEnabled = isVotingEnabled;
        this.viewCount = viewCount;
    }

    public static Post of(PostRequest postRequest, User user) {
        return Post.builder()
                .title(postRequest.getTitle())
                .contents(postRequest.getContents())
                .category(postRequest.getCategory())
                .isVotingEnabled(postRequest.isVotingEnabled())
                .user(user)
                .images(new ArrayList<>())
                .build();
    }

    public void updatePostDetails(String title, String contents, Category category, boolean isVotingEnabled) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.isVotingEnabled = isVotingEnabled;
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


}