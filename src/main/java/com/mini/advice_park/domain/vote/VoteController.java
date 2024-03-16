package com.mini.advice_park.domain.vote;

import com.mini.advice_park.domain.vote.entity.VoteOption;
import com.mini.advice_park.global.common.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote/{postId}")
public class VoteController {

    private final VoteService voteService;

    /**
     * 투표 등록
     */
    @PostMapping("")
    public ResponseEntity<BaseResponse<Void>> registerVote(@PathVariable("postId") Long postId,
                                                           @RequestBody VoteOption voteOption,
                                                           HttpServletRequest httpServletRequest) {

        voteService.createVote(postId, voteOption, httpServletRequest);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.CREATED, "투표 등록이 완료되었습니다.", null));
    }

    /**
     * 투표 상태 반환
     */
    @GetMapping("")
    public ResponseEntity<BaseResponse<VoteOption>> getVoteStatus(@PathVariable("postId") Long postId,
                                                                  HttpServletRequest httpServletRequest) {

        VoteOption voteOption = voteService.getVoteOption(postId, httpServletRequest);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "투표 상태 조회가 완료되었습니다.", voteOption));
    }

    /**
     * 투표 삭제
     */
    @DeleteMapping("")
    public ResponseEntity<BaseResponse<Void>> deleteVote(@PathVariable("postId") Long postId,
                                                         HttpServletRequest httpServletRequest) {

        voteService.deleteVote(postId, httpServletRequest);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "투표 삭제가 완료되었습니다.", null));
    }

}
