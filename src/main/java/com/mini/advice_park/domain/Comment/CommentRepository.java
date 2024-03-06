package com.mini.advice_park.domain.Comment;

import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    @Query("SELECT r FROM Comment r WHERE r.post.postId = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);

    void deleteByPost(Post post);

    void deleteByUser(User user);
    int countByPost(Post post);

    Optional<Comment> findByCommentIdAndPost(Long commentId, Post post);

    boolean existsByUserAndPost(User user, Post post);

}
