package com.mini.advice_park.domain.post.entity;

import com.mini.advice_park.domain.Image.Image;
import com.mini.advice_park.domain.post.dto.PostRequest;
import com.mini.advice_park.domain.user.entity.User;
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
public class Post {

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

    private boolean option; // 찬/반 사용여부

    @Builder
    public Post(String title,
                String contents,
                Category category,
                User user,
                List<Image> images,
                boolean option) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.user = user;
        if (images != null) {
            this.images.addAll(images);
        }
        this.option = option;
    }

    public static Post of(PostRequest postRequest, User user) {
        return Post.builder()
                .title(postRequest.getTitle())
                .contents(postRequest.getContents())
                .category(postRequest.getCategory())
                .option(postRequest.isOption())
                .user(user)
                .images(new ArrayList<>())
                .build();
    }

    public void updatePostDetails(String title, String contents, Category category, boolean option) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.option = option;
    }

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void clearImages() {
        this.images.clear();
    }


}
