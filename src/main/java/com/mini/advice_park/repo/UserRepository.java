package com.mini.advice_park.repo;

import com.mini.advice_park.domain.User;
import com.mini.advice_park.type.ELoginProvider;
import com.mini.advice_park.type.EUserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialIdAndRefreshTokenAndIsLogin(String socialId, String refreshToken, Boolean isLogin);

    Optional<User> findBySocialId(String socialId);
    @Query("select u.socialId as id, u.userType as userType, u.password as password from User u where u.socialId = :socialId")
    Optional<UserSecurityForm> findSecurityFormBySocialId(String socialId);

    Optional<User> findBySocialIdAndRefreshToken(String socialId, String refreshToken);

    Optional<User> findBySocialIdAndProvider(String socialId, ELoginProvider provider);

    @Query("select u from User u where u.socialId = :socialId and u.isLogin = :isLogin")
    Optional<User> findBySocialIdAndIsLogin(@Param("socialId") String socialId, @Param("isLogin") Boolean isLogin);

    @Query("select u.id as id, u.userType as userType, u.password as password from User u where u.id = :id")
    Optional<UserSecurityForm> findSecurityFormById(Long id);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.refreshToken = :refreshToken, u.isLogin = :isLogin where u.id = :id")
    void updateRefreshTokenAndLoginStatus(Long id, String refreshToken, Boolean isLogin);

    interface UserSecurityForm {
        String getId();
        EUserType getUserType();
        String getPassword();
    }
}