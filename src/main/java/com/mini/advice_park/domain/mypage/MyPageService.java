package com.mini.advice_park.domain.mypage;

import com.mini.advice_park.domain.Comment.CommentRepository;
import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.favorite.dto.UserPostFavoriteDto;
import com.mini.advice_park.domain.favorite.entity.UserPostFavorite;
import com.mini.advice_park.domain.favorite.UserPostFavoriteRepository;
import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.service.AuthService;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final AuthService authService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserPostFavoriteRepository favoriteRepository;


    /**
     * 등록글 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<PostResponse>> getPostsByCurrentUser(HttpServletRequest httpServletRequest) {

        User user = authService.getCurrentUser(httpServletRequest);

        List<Post> posts = postRepository.findByUser(user);
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

        User user = authService.getCurrentUser(httpServletRequest);

        List<Comment> comments = commentRepository.findByUser(user);
        if (comments.isEmpty()) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "사용자의 댓글이 없습니다.", null);
        }

        List<CommentResponse> commentDtos = comments.stream()
                .map(CommentResponse::from).collect(Collectors.toList());

        return new BaseResponse<>(HttpStatus.OK, "조회 성공", commentDtos);
    }

    /**
     * 내가 등록한 즐겨찾기 조회
     */
    @Transactional(readOnly = true)
    public List<UserPostFavoriteDto> getFavoritePosts(HttpServletRequest httpServletRequest) {

        User user = authService.getCurrentUser(httpServletRequest);

        List<UserPostFavorite> userPostFavorites = favoriteRepository.findByUser(user);

        return userPostFavorites.stream()
                .map(UserPostFavoriteDto::fromEntity)
                .collect(Collectors.toList());
    }

}
