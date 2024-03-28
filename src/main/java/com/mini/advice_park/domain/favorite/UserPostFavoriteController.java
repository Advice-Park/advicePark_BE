package com.mini.advice_park.domain.favorite;

import com.mini.advice_park.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
@Tag(name = "즐겨찾기 API", description = "즐겨찾기 API")
public class UserPostFavoriteController {

    private final UserPostFavoriteService favoriteService;

    @Operation(summary = "즐겨찾기 추가", description = "게시글을 즐겨찾기에 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "즐겨찾기 추가 성공"),
            @ApiResponse(responseCode = "400", description = "즐겨찾기 추가 실패"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Void>> addFavorite(
            HttpServletRequest httpServletRequest,
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @RequestParam Long postId) {

        BaseResponse<Void> response = favoriteService.addFavorite(httpServletRequest, postId).getBody();

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "즐겨찾기 상태 확인", description = "게시글의 즐겨찾기 상태를 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨찾기 상태 확인 성공"),
            @ApiResponse(responseCode = "400", description = "즐겨찾기 상태 확인 실패"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("")
    public ResponseEntity<BaseResponse<Boolean>> checkFavoriteStatus(
            HttpServletRequest httpServletRequest,
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @RequestParam Long postId) {

        boolean isFavorite = favoriteService.isFavorite(postId, httpServletRequest);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "즐겨찾기 상태 확인 성공", isFavorite));
    }

    @Operation(summary = "즐겨찾기 삭제", description = "게시글을 즐겨찾기에서 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨찾기 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "즐겨찾기 삭제 실패"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/remove")
    public ResponseEntity<BaseResponse<Void>> removeFavorite(
            HttpServletRequest httpServletRequest,
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @RequestParam Long postId) {

        BaseResponse<Void> response = favoriteService.removeFavorite(httpServletRequest, postId).getBody();

        return ResponseEntity.status(response.getCode()).body(response);
    }

}
