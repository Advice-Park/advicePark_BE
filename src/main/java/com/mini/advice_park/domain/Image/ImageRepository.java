package com.mini.advice_park.domain.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.post.id = :postId")
    List<Image> findAllByPostId(@Param("postId") Long postId);

    @Query("SELECT i FROM Image i WHERE i.post.user.userId = :userId")
    List<Image> findByUserId(@Param("userId") Long userId);

    // 포스트 ID별 모든 이미지를 반환하는 쿼리
    @Query("SELECT i FROM Image i WHERE i.post.id IN :postIds")
    List<Image> findAllImagesByPostIds(@Param("postIds") List<Long> postIds);

}
