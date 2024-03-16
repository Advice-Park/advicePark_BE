package com.mini.advice_park.domain.vote;

import com.mini.advice_park.domain.oauth2.domain.OAuth2UserPrincipal;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
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
     * OAuth2로 인증된 사용자의 이메일 주소를 사용하여 검색
     */
    private User getUserFromAuthentication(Authentication authentication) {

        // OAuth2UserPrincipal 클래스를 이용하여 유저 정보를 가져옴
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();

        // OAuth2로 인증된 사용자의 이메일 주소를 사용하여 검색
        String email = principal.getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * 투표 등록
     */
    @PostMapping("")
    public ResponseEntity<BaseResponse<Void>> registerVote(@PathVariable("postId") Long postId,
                                                           @RequestBody VoteOption voteOption,
                                                           Authentication authentication) {

        User user = getUserFromAuthentication(authentication);
        voteService.createVote(user.getUserId(), postId, voteOption);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.CREATED, "투표 등록이 완료되었습니다.", null));
    }

    /**
     * 투표 상태 반환
     */
    @GetMapping("")
    public ResponseEntity<BaseResponse<VoteOption>> getVoteStatus(@PathVariable("postId") Long postId,
                                                                  Authentication authentication) {

        User user = getUserFromAuthentication(authentication);
        VoteOption voteOption = voteService.getVoteOption(user.getUserId(), postId);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "투표 상태 조회가 완료되었습니다.", voteOption));
    }

    /**
     * 투표 삭제
     */
    @DeleteMapping("")
    public ResponseEntity<BaseResponse<Void>> deleteVote(@PathVariable("postId") Long postId,
                                                         Authentication authentication) {

        User user = getUserFromAuthentication(authentication);
        voteService.deleteVote(user.getUserId(), postId);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "투표 삭제가 완료되었습니다.", null));
    }

}
