package com.mini.advice_park.domain.vote;

import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.vote.entity.Vote;
import com.mini.advice_park.domain.vote.entity.VoteOption;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    /**
     * 사용자 조회
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
    }

    /**
     * 투표 등록
     */
    @Transactional
    public void createVote(Long userId, Long postId, VoteOption voteOption) {
        User user = findUserById(userId);
        Post post = findPostById(postId);

        voteRepository.findByUserAndPost(user, post)
                .ifPresent(vote -> { throw new CustomException(ErrorCode.ALREADY_VOTED); });

        voteRepository.save(new Vote(user, voteOption, post));
    }

    /**
     * 투표 상태 반환
     */
    @Transactional(readOnly = true)
    public VoteOption getVoteOption(Long userId, Long postId) {
        User user = findUserById(userId);
        Post post = findPostById(postId);

        return voteRepository.findByUserAndPost(user, post)
                .map(Vote::getVoteOption)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOTE));
    }

    /**
     * 투표 삭제
     */
    @Transactional
    public void deleteVote(Long userId, Long postId) {
        User user = findUserById(userId);
        Post post = findPostById(postId);

        Vote vote = voteRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOTE));

        voteRepository.delete(vote);
    }
}
