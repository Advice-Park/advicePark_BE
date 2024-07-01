package com.mini.advice_park.domain.post;

import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user);

    List<Post> findByTitleContaining(String title);

    List<Post> findByContentsContaining(String contents);

    void deleteAllByUser(User user);

    @Query("SELECT p.id FROM Post p")
    List<Long> findAllPostIds(); // 전체 포스트 ID를 조회하는 메서드

}
