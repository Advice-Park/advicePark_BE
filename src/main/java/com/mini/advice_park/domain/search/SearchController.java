package com.mini.advice_park.domain.search;

import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.post.dto.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@Tag(name = "검색 API", description = "검색 API")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "게시글 검색", description = "검색어로 게시글 검색")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 검색 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/post")
    public ResponseEntity<List<PostResponse>> searchPosts(
            @Parameter(description = "검색어", required = true, example = "검색어")
            @RequestParam("keyword") String keyword) {

        List<PostResponse> searchResults = searchService.searchPosts(keyword);
        return ResponseEntity.ok(searchResults);
    }

    @Operation(summary = "댓글 검색", description = "검색어로 댓글 검색")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 검색 성공"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/comment")
    public ResponseEntity<List<CommentResponse>> searchComments(
            @Parameter(description = "검색어", required = true, example = "검색어")
            @RequestParam("keyword") String keyword) {

        List<CommentResponse> searchResults = searchService.searchComments(keyword);
        return ResponseEntity.ok(searchResults);
    }

}
