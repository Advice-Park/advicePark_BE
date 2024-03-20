package com.mini.advice_park.domain.post;

import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user);

    // 사용자와 관련된 속성을 사용하지 않도록 수정
    List<Post> findByTitleContaining(String title);

    // 사용자와 관련된 속성을 사용하지 않도록 수정
    List<Post> findByContentsContaining(String contents);
}
