package com.mini.advice_park.domain.search;

import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.post.dto.PostResponse;
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

    /**
     * 검색어로 게시글 검색
     */
    @GetMapping("/post")
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam("keyword") String keyword) {
        List<PostResponse> searchResults = searchService.searchPosts(keyword);
        return ResponseEntity.ok(searchResults);
    }

    /**
     * 검색어로 댓글 검색
     */
    @GetMapping("/comment")
    public ResponseEntity<List<CommentResponse>> searchComments(@RequestParam("keyword") String keyword) {
        List<CommentResponse> searchResults = searchService.searchComments(keyword);
        return ResponseEntity.ok(searchResults);
    }

}
