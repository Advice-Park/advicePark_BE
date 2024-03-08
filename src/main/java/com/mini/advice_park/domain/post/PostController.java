package com.mini.advice_park.domain.post;

import com.mini.advice_park.domain.post.PostService;
import com.mini.advice_park.domain.post.dto.PostRequest;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.common.LoginAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    /**
     * 질문글 등록
     */
    @PostMapping("")
    public ResponseEntity<BaseResponse<PostResponse>> createPost(@ModelAttribute PostRequest postRequest,
                                                                 @RequestPart(value = "imageFiles",
                                                                         required = false) List<MultipartFile> imageFiles) {
        BaseResponse<PostResponse> response = postService.createPost(postRequest, imageFiles);
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
