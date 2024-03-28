package com.mini.advice_park.domain.mypage;

import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.favorite.dto.UserPostFavoriteDto;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Tag(name = "마이페이지 API", description = "마이페이지 API")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "내가 작성한 게시글 조회", description = "내가 작성한 게시글을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/post")
    public ResponseEntity<BaseResponse<List<PostResponse>>> getPostByCurrentUser(HttpServletRequest httpServletRequest) {

        BaseResponse<List<PostResponse>> response = myPageService.getPostsByCurrentUser(httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "내가 작성한 댓글 조회", description = "내가 작성한 댓글을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/comment")
    public ResponseEntity<BaseResponse<List<CommentResponse>>> getCommentsByCurrentUser(HttpServletRequest httpServletRequest) {

        BaseResponse<List<CommentResponse>> response = myPageService.getCommentsByCurrentUser(httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "즐겨찾기 목록 조회", description = "즐겨찾기한 게시글 목록을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨찾기 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "즐겨찾기한 게시글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/favorite")
    public ResponseEntity<BaseResponse<List<UserPostFavoriteDto>>> getFavoritePosts(HttpServletRequest httpServletRequest) {

        List<UserPostFavoriteDto> favoritePostDTOs = myPageService.getFavoritePosts(httpServletRequest);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "즐겨찾기 목록 조회 성공", favoritePostDTOs));
    }

}
