package com.mini.advice_park.domain.favorite;

import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.global.common.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class UserPostFavoriteController {

    private final UserPostFavoriteService favoriteService;

    /**
     * 즐겨찾기 추가
     */
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Void>> addFavorite(HttpServletRequest httpServletRequest,
                                                          @RequestParam Long postId) {

        BaseResponse<Void> response = favoriteService.addFavorite(httpServletRequest, postId).getBody();

        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * 즐겨찾기 조회
     */
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<Post>>> getFavoritePosts(HttpServletRequest httpServletRequest) {

        List<Post> favoritePosts = favoriteService.getFavoritePosts(httpServletRequest);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "즐겨찾기 목록 조회 성공", favoritePosts));
    }

    /**
     * 즐겨찾기 삭제
     */
    @DeleteMapping("/remove")
    public ResponseEntity<BaseResponse<Void>> removeFavorite(HttpServletRequest httpServletRequest,
                                                             @RequestParam Long postId) {

        BaseResponse<Void> response = favoriteService.removeFavorite(httpServletRequest, postId).getBody();

        return ResponseEntity.status(response.getCode()).body(response);
    }

}
