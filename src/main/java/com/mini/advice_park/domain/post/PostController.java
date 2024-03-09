package com.mini.advice_park.domain.post;

import com.mini.advice_park.domain.oauth2.CustomOAuth2UserService;
import com.mini.advice_park.domain.oauth2.domain.OAuth2UserPrincipal;
import com.mini.advice_park.domain.post.PostService;
import com.mini.advice_park.domain.post.dto.PostRequest;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.common.LoginAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * 질문글 등록
     */
    @PostMapping("")
    public ResponseEntity<BaseResponse<PostResponse>> createPost(@ModelAttribute PostRequest postRequest,
                                                                 @RequestPart(value = "imageFiles",
                                                                         required = false) List<MultipartFile> imageFiles,
                                                                 @AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal) {

        User loginUser = (User) customOAuth2UserService.loadUser(oAuth2UserPrincipal);

        BaseResponse<PostResponse> response = postService.createPost(postRequest, imageFiles, (OAuth2User) loginUser);
        return ResponseEntity.status(response.getCode()).body(response);
    }


    /**
     * 질문글 전체 조회
     */
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<PostResponse>>> getAllPosts() {
        BaseResponse<List<PostResponse>> response = postService.getAllPosts();
        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * 특정 질문글 조회
     */
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponse>> getPostById(@PathVariable Long postId) {
        BaseResponse<PostResponse> response = postService.getPostById(postId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * 질문글 삭제
     */
    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('ADMIN') or @postService.isPostOwner(#postId, principal.id)")
    public ResponseEntity<BaseResponse<Void>> deletePost(@PathVariable Long postId) {
        BaseResponse<Void> response = postService.deletePost(postId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

}
