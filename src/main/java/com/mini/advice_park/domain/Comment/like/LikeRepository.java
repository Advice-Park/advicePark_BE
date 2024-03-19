package com.mini.advice_park.domain.Comment.like;

import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndComment(User user, Comment comment);

    // 댓글에 대한 좋아요 수를 조회하는 메서드
    int countByComment(Comment comment);

    // 사용자와 댓글에 대한 좋아요를 삭제하는 메서드 추가
    void deleteByUserAndComment(User user, Comment comment);

    List<Like> findByCommentIn(List<Comment> comments);
}
