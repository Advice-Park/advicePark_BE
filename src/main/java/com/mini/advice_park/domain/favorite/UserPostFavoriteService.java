package com.mini.advice_park.domain.favorite;

import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.global.security.filter.JwtAuthorizationFilter;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserPostFavoriteService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserPostFavoriteRepository favoriteRepository;

    /**
     * 현재 사용자 정보 가져오기
     */
    private User getCurrentUser(HttpServletRequest httpServletRequest) {

        String token = JwtAuthorizationFilter.resolveToken(httpServletRequest);
        if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        String email = jwtUtil.getEmail(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ERROR));
    }

    /**
     * 즐겨찾기 추가
     */
    @Transactional
    public ResponseEntity<BaseResponse<Void>> addFavorite(HttpServletRequest httpServletRequest, Long postId) {

        User user = getCurrentUser(httpServletRequest);

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

        User user = getCurrentUser(httpServletRequest);

        // 수정 후
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
