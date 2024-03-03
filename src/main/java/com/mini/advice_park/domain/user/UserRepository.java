package com.mini.advice_park.domain.user;

import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.user.entity.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT m FROM User m WHERE m.email = :email AND m.oAuth2Provider = :provider")
    Optional<User> findByEmailAndOAuth2Provider(@Param("email") String email, @Param("provider") OAuth2Provider oAuth2Provider);

}
