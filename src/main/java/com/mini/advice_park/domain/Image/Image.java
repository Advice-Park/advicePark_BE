package com.mini.advice_park.domain.Image;

import com.mini.advice_park.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "images")
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String storedImagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Image(String originName, String storedImagePath, Post post) {
        this.originName = originName;
        this.storedImagePath = storedImagePath;
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
