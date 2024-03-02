package com.mini.advice_park.member.repository;

import com.mini.advice_park.member.domain.Member;
import com.mini.advice_park.member.domain.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.oAuth2Provider = :provider")
    Optional<Member> findByEmailAndOAuth2Provider(@Param("email") String email, @Param("provider") OAuth2Provider oAuth2Provider);

}
