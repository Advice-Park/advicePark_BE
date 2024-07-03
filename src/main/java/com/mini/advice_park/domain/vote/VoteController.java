package com.mini.advice_park.domain.vote;

import com.mini.advice_park.domain.vote.entity.VoteOption;
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
@RequestMapping("/api/vote/{postId}")
@Tag(name = "투표 API", description = "투표 API")
public class VoteController {

    private final VoteService voteService;

    @Operation(summary = "투표 상태 조회", description = "투표 상태를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 상태 조회 성공"),
            @ApiResponse(responseCode = "400", description = "투표 상태 조회 실패"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("")
    public ResponseEntity<BaseResponse<VoteOption>> getVoteStatus(
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        VoteOption voteOption = voteService.getVoteOption(postId, httpServletRequest);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "투표 상태 조회가 완료되었습니다.", voteOption));
    }

    @Operation(summary = "사용자의 투표 상태 조회", description = "사용자가 투표를 찬성했는지 반대했는지 조회")
    @GetMapping("/user")
    public ResponseEntity<BaseResponse<String>> getUserVoteStatus(
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        String userVoteStatus = voteService.getUserVoteStatus(postId, httpServletRequest);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "사용자의 투표 상태 조회가 완료되었습니다.", userVoteStatus));
    }

    @Operation(summary = "찬성 투표 등록", description = "찬성 투표를 등록")
    @PostMapping("/support")
    public ResponseEntity<BaseResponse<Void>> registerSupportVote(
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        voteService.createVote(postId, VoteOption.SUPPORT, httpServletRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(HttpStatus.CREATED, "찬성 투표 등록이 완료되었습니다.", null));
    }

    @Operation(summary = "찬성 투표 삭제", description = "찬성 투표를 삭제")
    @DeleteMapping("/support")
    public ResponseEntity<BaseResponse<Void>> deleteSupportVote(
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        voteService.deleteVote(postId, httpServletRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new BaseResponse<>(HttpStatus.NO_CONTENT, "찬성 투표 삭제가 완료되었습니다.", null));
    }

    @Operation(summary = "반대 투표 등록", description = "반대 투표를 등록")
    @PostMapping("/oppose")
    public ResponseEntity<BaseResponse<Void>> registerOpposeVote(
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        voteService.createVote(postId, VoteOption.OPPOSE, httpServletRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(HttpStatus.CREATED, "반대 투표 등록이 완료되었습니다.", null));
    }

    @Operation(summary = "반대 투표 삭제", description = "반대 투표를 삭제")
    @DeleteMapping("/oppose")
    public ResponseEntity<BaseResponse<Void>> deleteOpposeVote(
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        voteService.deleteVote(postId, httpServletRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new BaseResponse<>(HttpStatus.NO_CONTENT, "반대 투표 삭제가 완료되었습니다.", null));
    }

}
