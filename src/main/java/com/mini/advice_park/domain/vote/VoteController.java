package com.mini.advice_park.domain.vote;

import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote/{postId}")
public class VoteController {

    private final VoteService voteService;
    private final UserRepository userRepository;

    /**
     * 특정 게시물에 대한 투표 등록
     */
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Void>> registerVote(@PathVariable("postId") Long postId,
                                                           @RequestParam("voteOption") VoteOption voteOption,
                                                           Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);

        voteService.createVote(userId, postId, voteOption);
        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.CREATED, "투표 등록이 완료되었습니다.", null));
    }

    /**
     * 특정 게시물에 대한 투표 상태 조회
     */
    @GetMapping("/status")
    public ResponseEntity<BaseResponse<VoteOption>> getVoteStatus(@PathVariable("postId") Long postId,
                                                                  Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);

        VoteOption voteOption = voteService.getVoteOption(userId, postId);
        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "투표 상태 조회가 완료되었습니다.", voteOption));
    }

    /**
     * 인증된 사용자의 ID 가져오기
     */
    private Long getUserIdFromAuthentication(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER))
                .getUserId();
    }

}
