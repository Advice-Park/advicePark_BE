package com.mini.advice_park.domain.Comment.like;

import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndComment(User user, Comment comment);
    void deleteByUser(User user);
    int countByComment(Comment comment);

}