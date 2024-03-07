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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /**
     * 등록글 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<PostResponse>> getPostsByCurrentUser(String providerId) {
        // providerId로 사용자를 찾음
        User currentUser = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        List<Post> posts = postRepository.findByUser(currentUser);
        if (posts.isEmpty()) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "사용자의 글이 없습니다.", null);
        }

        // 댓글 수를 포함한 PostResponse 객체 생성
        List<PostResponse> postDtos = posts.stream()
                .map(PostResponse::from).collect(Collectors.toList());

        return new BaseResponse<>(HttpStatus.OK, "조회 성공", postDtos);
    }

    /**
     * 내가 작성한 댓글 모두 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<CommentResponse>> getCommentsByCurrentUser(String providerId) {
        // providerId로 사용자를 찾음
        User currentUser = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        List<Comment> comments = commentRepository.findByUser(currentUser);
        if (comments.isEmpty()) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "사용자의 댓글이 없습니다.", null);
        }

        // 댓글 Response 객체 생성
        List<CommentResponse> commentDtos = comments.stream()
                .map(CommentResponse::from).collect(Collectors.toList());

        return new BaseResponse<>(HttpStatus.OK, "조회 성공", commentDtos);
    }

}
