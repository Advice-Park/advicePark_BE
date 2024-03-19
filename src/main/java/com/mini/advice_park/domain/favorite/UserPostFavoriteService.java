package com.mini.advice_park.domain.favorite;

import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.service.AuthService;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPostFavoriteService {

    private final AuthService authService;
    private final PostRepository postRepository;
    private final UserPostFavoriteRepository favoriteRepository;


    /**
     * 즐겨찾기 추가
     */
    @Transactional
    public ResponseEntity<BaseResponse<Void>> addFavorite(HttpServletRequest httpServletRequest, Long postId) {

        User user = authService.getCurrentUser(httpServletRequest);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        if (!favoriteRepository.existsByUserAndPost(user, post)) {
            throw new CustomException(ErrorCode.NOT_FAVORITE);
        }

        UserPostFavorite favorite = UserPostFavorite.builder()
                .user(user)
                .post(post)
                .build();
        favoriteRepository.save(favorite);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(HttpStatus.CREATED, "즐겨찾기 추가 성공", null));
    }

    /**
     * 즐겨찾기 삭제
     */
    @Transactional
    public ResponseEntity<BaseResponse<Void>> removeFavorite(HttpServletRequest httpServletRequest, Long postId) {

        User user = authService.getCurrentUser(httpServletRequest);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        if (!favoriteRepository.existsByUserAndPost(user, post)) {
            throw new CustomException(ErrorCode.NOT_FAVORITE);
        }

        favoriteRepository.deleteByUserAndPost(user, post);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new BaseResponse<>(HttpStatus.NO_CONTENT, "즐겨찾기 삭제 성공", null));
    }

}
