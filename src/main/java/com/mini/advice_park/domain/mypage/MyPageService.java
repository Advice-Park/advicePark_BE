package com.mini.advice_park.domain.mypage;

import com.mini.advice_park.domain.Comment.CommentRepository;
import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.dto.PostResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /**
     * 등록글 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<PostResponse>> getPostsByCurrentUser(HttpServletRequest httpServletRequest) {

        // 1. JWT 토큰을 이용하여 사용자 인증 확인
        String token = JwtAuthorizationFilter.resolveToken(httpServletRequest);
        if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        // 2. JWT 토큰에서 사용자 정보 추출
        String email = jwtUtil.getEmail(token);
        User loginUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ERROR));

        List<Post> posts = postRepository.findByUser(loginUser);
        if (posts.isEmpty()) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "사용자의 글이 없습니다.", null);
        }

        List<PostResponse> postDtos = posts.stream()
                .map(PostResponse::from).collect(Collectors.toList());

        return new BaseResponse<>(HttpStatus.OK, "조회 성공", postDtos);
    }

    /**
     * 내가 작성한 댓글 모두 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<CommentResponse>> getCommentsByCurrentUser(HttpServletRequest httpServletRequest) {

        // 1. JWT 토큰을 이용하여 사용자 인증 확인
        String token = JwtAuthorizationFilter.resolveToken(httpServletRequest);
        if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        // 2. JWT 토큰에서 사용자 정보 추출
        String email = jwtUtil.getEmail(token);
        User loginUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ERROR));


        List<Comment> comments = commentRepository.findByUser(loginUser);
        if (comments.isEmpty()) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "사용자의 댓글이 없습니다.", null);
        }

        // 댓글 Response 객체 생성
        List<CommentResponse> commentDtos = comments.stream()
                .map(CommentResponse::from).collect(Collectors.toList());

        return new BaseResponse<>(HttpStatus.OK, "조회 성공", commentDtos);
    }

}
