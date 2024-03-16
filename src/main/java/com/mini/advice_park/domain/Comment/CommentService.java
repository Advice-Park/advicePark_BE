package com.mini.advice_park.domain.Comment;

import com.mini.advice_park.domain.Comment.dto.CommentRequest;
import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.Comment.like.LikeRepository;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 등록
     */
    @Transactional
    public BaseResponse<CommentResponse> createComment(Long postId,
                                                       @Valid CommentRequest commentRequest,
                                                       HttpServletRequest httpServletRequest) {

        try {
            String token = JwtAuthorizationFilter.resolveToken(httpServletRequest);
            if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
            }

            String email = jwtUtil.getEmail(token);
            User loginUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ERROR));

            Comment comment = Comment.builder()
                    .content(commentRequest.getContent())
                    .user(loginUser)
                    .post(postRepository.findById(postId)
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST)))
                    .build();

            comment = commentRepository.save(comment);

            return new BaseResponse<>(HttpStatus.CREATED, "성공", CommentResponse.from(comment));

        } catch (DataAccessException e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.DATA_BASE_ERROR.getMessage(), null);

        } catch (CustomException e) {
            return new BaseResponse<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), null);
        }
    }

    /**
     * 모든 댓글 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<CommentResponse>> getAllComments(Long postId) {

        // 게시물 정보 가져오기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 게시물에 달린 댓글들 가져오기
        List<Comment> comments = commentRepository.findByPostId(postId);

        // 댓글들을 CommentResponse로 변환
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> {
                    int likeCount = likeRepository.countByComment(comment);

                    return new CommentResponse(
                            comment.getCommentId(),
                            comment.getUser().getUserId(),
                            comment.getPost().getPostId(),
                            comment.getContent(),
                            0,
                            comment.getCreatedTime());
                }).collect(Collectors.toList());

        return new BaseResponse<>(HttpStatus.OK, "성공", commentResponses);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public BaseResponse<Void> deleteComment(Long postId, Long commentId, HttpServletRequest httpServletRequest) {

        try {
            // 1. JWT 토큰을 이용하여 사용자 인증 확인
            String token = JwtAuthorizationFilter.resolveToken(httpServletRequest);
            if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
            }

            // 2. JWT 토큰에서 사용자 정보 추출
            String email = jwtUtil.getEmail(token);
            User loginUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ERROR));

            // 3. 게시물 조회
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

            // 4. 게시물에 달린 댓글 정보 조회
            Comment comment = commentRepository.findByCommentIdAndPost(commentId, post)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

            // 5. 댓글 작성자와 로그인한 사용자 일치 여부 확인
            if (!comment.getUser().equals(loginUser)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
            }

            // 6. 댓글 삭제
            commentRepository.delete(comment);

            return new BaseResponse<>(HttpStatus.OK, "성공", null);
        } catch (CustomException e) {
            return new BaseResponse<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), null);
        }
    }

}
