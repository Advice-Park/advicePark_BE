package com.mini.advice_park.domain.vote;

import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.vote.entity.Vote;
import com.mini.advice_park.domain.vote.entity.VoteOption;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.global.security.filter.JwtAuthorizationFilter;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    /**
     * 현재 사용자 정보 가져오기
     */
    private User getCurrentUser(HttpServletRequest httpServletRequest) {

        String token = JwtAuthorizationFilter.resolveToken(httpServletRequest);
        if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        String email = jwtUtil.getEmail(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ERROR));
    }

    /**
     * 투표 등록
     */
    @Transactional
    public void createVote(Long postId, VoteOption voteOption, HttpServletRequest httpServletRequest) {

        User user = getCurrentUser(httpServletRequest);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        voteRepository.findByUserAndPost(user, post)
                .ifPresent(vote -> { throw new CustomException(ErrorCode.ALREADY_VOTED); });

        voteRepository.save(new Vote(user, voteOption, post));
    }

    /**
     * 투표 상태 반환
     */
    @Transactional(readOnly = true)
    public VoteOption getVoteOption(Long postId, HttpServletRequest httpServletRequest) {

        User user = getCurrentUser(httpServletRequest);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        return voteRepository.findByUserAndPost(user, post)
                .map(Vote::getVoteOption)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOTE));
    }

    /**
     * 투표 삭제
     */
    @Transactional
    public void deleteVote(Long postId, HttpServletRequest httpServletRequest) {

        User user = getCurrentUser(httpServletRequest);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        Vote vote = voteRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOTE));

        voteRepository.delete(vote);
    }

}
