package com.mini.advice_park.domain.vote;

import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserAndPost(User user, Post post);

}
