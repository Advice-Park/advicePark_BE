package com.mini.advice_park.domain.Comment.like;

import com.mini.advice_park.domain.Comment.CommentRepository;
import com.mini.advice_park.domain.Comment.entity.Comment;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    /**
     * 좋아요 등록
     */
    @Transactional
    public BaseResponse<Void> createLike(Long commentId, HttpServletRequest httpServletRequest) {
        // 현재 사용자 정보 가져오기
        User user = getCurrentUser(httpServletRequest);

        // 댓글 가져오기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        // 이미 좋아요를 등록한 경우
        if (likeRepository.findByUserAndComment(user, comment).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }

        // 좋아요 등록
        likeRepository.save(new Like(user, comment));

        return new BaseResponse<>(HttpStatus.CREATED, "좋아요 등록 성공", null);
    }

    /**
     * 좋아요 상태 반환
     */
    @Transactional(readOnly = true)
    public boolean isLiked(Long commentId, HttpServletRequest httpServletRequest) {
        // 현재 사용자 정보 가져오기
        User user = getCurrentUser(httpServletRequest);

        // 댓글 가져오기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        // 해당 댓글에 좋아요를 등록한 경우 true 반환
        return likeRepository.findByUserAndComment(user, comment).isPresent();
    }

    /**
     * 좋아요 삭제
     */
    @Transactional
    public BaseResponse<Void> deleteLike(Long commentId, HttpServletRequest httpServletRequest) {
        // 현재 사용자 정보 가져오기
        User user = getCurrentUser(httpServletRequest);

        // 댓글 가져오기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        // 해당 댓글에 좋아요가 없는 경우
        if (!likeRepository.findByUserAndComment(user, comment).isPresent()) {
            throw new CustomException(ErrorCode.NOT_FOUND_LIKE);
        }

        // 좋아요 삭제
        likeRepository.deleteByUserAndComment(user, comment);

        return new BaseResponse<>(HttpStatus.OK, "좋아요 삭제 성공", null);
    }

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

}
