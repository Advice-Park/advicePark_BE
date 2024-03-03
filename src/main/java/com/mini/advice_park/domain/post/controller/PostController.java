package com.mini.advice_park.domain.post.controller;

import com.mini.advice_park.domain.post.PostService;
import com.mini.advice_park.domain.post.dto.PostRequest;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.util.BaseResponse;
import com.mini.advice_park.global.util.LoginAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<BaseResponse<PostResponse>> createPost(@RequestBody PostRequest postRequest,
                                                                 @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                                                                 @LoginAccount User currentUser) {
        BaseResponse<PostResponse> response = postService.createPost(postRequest, imageFiles, currentUser);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * 질문글 전체 조회
     */

    /**
     * 질문글 삭제
     */

}
