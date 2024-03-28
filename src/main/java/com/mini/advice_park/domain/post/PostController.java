package com.mini.advice_park.domain.post;

import com.mini.advice_park.domain.post.dto.PostRequest;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@Tag(name = "질문글 API", description = "질문글 API")
public class PostController {

    private final PostService postService;

    @Operation(summary = "질문글 작성", description = "질문글을 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "질문글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "질문글 작성 실패"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("")
    public ResponseEntity<BaseResponse<PostResponse>> createPost(@Valid @ModelAttribute PostRequest postRequest,
                                                                 @RequestPart(value = "imageFiles",
                                                                         required = false) List<MultipartFile> imageFiles,
                                                                 HttpServletRequest httpServletRequest) {

        BaseResponse<PostResponse> response = postService.createPost(postRequest, imageFiles, httpServletRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(response.getCode(), response.getMessage(), response.getResult()));
    }

    @Operation(summary = "모든 질문글 조회", description = "모든 질문글을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "질문글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "질문글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<PostResponse>>> getAllPosts() {

        BaseResponse<List<PostResponse>> response = postService.getAllPosts();

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "특정 질문글 조회", description = "특정 질문글을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "질문글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "질문글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponse>> getPostById(
            @Parameter(description = "질문글 ID", required = true, example = "1")
            @PathVariable Long postId) {

        BaseResponse<PostResponse> response = postService.getPostById(postId);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "질문글 삭제", description = "질문글을 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "질문글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "질문글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> deletePost(
            @Parameter(description = "질문글 ID", required = true, example = "1")
            @PathVariable Long postId,
            HttpServletRequest httpServletRequest) {

        BaseResponse<Void> response = postService.deletePost(postId, httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

}
