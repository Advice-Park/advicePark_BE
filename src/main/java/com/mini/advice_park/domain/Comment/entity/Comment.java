package com.mini.advice_park.domain.Comment.entity;

import com.mini.advice_park.domain.Comment.like.Like;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private Set<Like> likes;

    // @Transient // DB에 매핑하지 않음
    private int likeCount;

    @Builder
    public Comment(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.likes = new HashSet<>();
        this.likeCount = 0; // 초기값 설정
    }

    public static Comment of(String content, User user, Post post) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
    }

    // 좋아요 카운트 증가 메서드
    public void incrementLikeCount() {
        if (likes.stream().noneMatch(like -> like.getUser().equals(user))) {
            likes.add(new Like(user, this)); // 현재 댓글에 대한 좋아요 추가
            updateLikeCount(); // 좋아요 수 업데이트
        }
    }

    // 좋아요 카운트 감소 메서드
    public void decrementLikeCount() {
        likes.removeIf(like -> like.getUser().equals(user)); // 현재 사용자의 좋아요 제거
        updateLikeCount(); // 좋아요 수 업데이트
    }

    // 좋아요 수를 업데이트하는 메서드
    public void updateLikeCount() {
        this.likeCount = likes.size();
    }

}
