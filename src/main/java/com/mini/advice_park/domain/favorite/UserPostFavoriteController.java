package com.mini.advice_park.domain.favorite;

import com.mini.advice_park.global.common.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
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
     * 즐겨찾기 삭제
     */
    @DeleteMapping("/remove")
    public ResponseEntity<BaseResponse<Void>> removeFavorite(HttpServletRequest httpServletRequest,
                                                             @RequestParam Long postId) {

        BaseResponse<Void> response = favoriteService.removeFavorite(httpServletRequest, postId).getBody();

        return ResponseEntity.status(response.getCode()).body(response);
    }

}
