package com.mini.advice_park.domain.favorite;

import com.mini.advice_park.domain.favorite.entity.UserPostFavorite;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPostFavoriteRepository extends JpaRepository<UserPostFavorite, Long> {

    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    List<UserPostFavorite> findByUser(User user);
    List<UserPostFavorite> findByPost(Post post);
    Optional<UserPostFavorite> findByUserAndPost(User user, Post post);

}
