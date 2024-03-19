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
    private final CommentRepository commentRepository;

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
     * 게시물에서 댓글 조회
     */
    private Comment getComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        return commentRepository.findByCommentIdAndPost(commentId, post)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
    }

    /**
     * 게시물의 댓글 수 감소
     */
    private void decreaseCommentCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        post.decreaseCommentCount();
        postRepository.save(post);
    }

    /**
     * 댓글 등록
     */
    @Transactional
    public BaseResponse<CommentResponse> createComment(Long postId,
                                                       @Valid CommentRequest commentRequest,
                                                       HttpServletRequest httpServletRequest) {

        try {
            // 현재 사용자 정보 가져오기
            User user = getCurrentUser(httpServletRequest);

            // 게시물 조회
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

            // 댓글 생성
            Comment comment = Comment.builder()
                    .content(commentRequest.getContent())
                    .user(user)
                    .post(post)
                    .build();

            // 댓글 저장
            comment = commentRepository.save(comment);

            // 게시물의 댓글 수 증가
            post.increaseCommentCount();
            postRepository.save(post);

            // 댓글 생성 성공 응답 반환
            return new BaseResponse<>(HttpStatus.CREATED, "댓글 등록 성공", CommentResponse.from(comment));

        } catch (DataAccessException e) {
            // 데이터베이스 접근 오류가 발생한 경우
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.DATA_BASE_ERROR.getMessage(), null);

        } catch (CustomException e) {
            // 커스텀 예외 발생 시 해당하는 에러코드와 메시지를 응답으로 반환
            return new BaseResponse<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), null);
        }
    }

    /**
     * 특정 게시물의 모든 댓글 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<CommentResponse>> getAllComments(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        List<Comment> comments = commentRepository.findByPostId(postId);

        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> CommentResponse.from(comment))
                .collect(Collectors.toList());

        return new BaseResponse<>(HttpStatus.OK, "성공", commentResponses);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public BaseResponse<Void> deleteComment(Long postId, Long commentId, HttpServletRequest httpServletRequest) {
        try {
            // 사용자 인증 및 권한 확인
            User loginUser = getCurrentUser(httpServletRequest);

            // 댓글 조회
            Comment comment = getComment(postId, commentId);

            // 권한 확인: 댓글 작성자만 삭제 가능
            if (!comment.getUser().equals(loginUser)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
            }

            // 댓글 삭제
            commentRepository.delete(comment);

            // 게시물의 댓글 수 감소
            decreaseCommentCount(postId);

            // 성공 응답 반환
            return new BaseResponse<>(HttpStatus.OK, "댓글 삭제 성공", null);
        } catch (CustomException e) {
            // 예외 발생 시 해당하는 에러코드와 메시지를 응답으로 반환
            return new BaseResponse<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), null);
        }
    }

}
