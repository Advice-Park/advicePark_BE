package com.mini.advice_park.domain.vote;

import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.user.service.AuthService;
import com.mini.advice_park.domain.vote.entity.Vote;
import com.mini.advice_park.domain.vote.entity.VoteOption;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final AuthService authService;
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    /**
     * 투표 상태 반환
     */
    @Transactional(readOnly = true)
    public VoteOption getVoteOption(Long postId, HttpServletRequest httpServletRequest) {
        User user = authService.getCurrentUser(httpServletRequest);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        return voteRepository.findByUserAndPost(user, post)
                .map(Vote::getVoteOption)
                .orElse(VoteOption.NONE);
    }

    /**
     * 투표 등록
     */
    @Transactional
    public void createVote(Long postId, VoteOption voteOption, HttpServletRequest httpServletRequest) {

        User user = authService.getCurrentUser(httpServletRequest);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        voteRepository.findByUserAndPost(user, post)
                .ifPresent(vote -> { throw new CustomException(ErrorCode.ALREADY_VOTED); });

        voteRepository.save(new Vote(user, voteOption, post));
    }

    /**
     * 투표 등록 또는 업데이트
     */
    @Transactional
    public void createOrUpdateVote(Long postId, VoteOption voteOption, HttpServletRequest httpServletRequest) {
        User user = authService.getCurrentUser(httpServletRequest);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        Vote vote = voteRepository.findByUserAndPost(user, post)
                .orElse(new Vote(user, VoteOption.NONE, post));

        vote.setVoteOption(voteOption);
        voteRepository.save(vote);
    }

    /**
     * 투표 삭제 (무투표 상태로 변경)
     */
    @Transactional
    public void deleteVote(Long postId, HttpServletRequest httpServletRequest) {
        User user = authService.getCurrentUser(httpServletRequest);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        Vote vote = voteRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOTE));

        vote.setVoteOption(VoteOption.NONE);
        voteRepository.save(vote);
    }

}
