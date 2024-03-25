package com.mini.advice_park.domain.user.service;

import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.dto.SignUpRequest;
import com.mini.advice_park.domain.user.dto.UserInfo;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 서비스
     */
    @Transactional
    public void signUp(SignUpRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String nickname = request.getNickname();
        String image = request.getImage();

        User user = User.of(email, password, firstName, lastName, nickname, image, passwordEncoder);

        userRepository.save(user);
    }

    /**
     * 특정 유저 정보 조회
     */
    @Transactional(readOnly = true)
    public UserInfo getUserInfo(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return UserInfo.of(
                user.getUserId(),
                user.getProviderId(),
                user.getRole(),
                user.getEmail(),
                user.getName(),
                user.getImage());
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public UserInfo getCurrentUserInfo(HttpServletRequest httpServletRequest) {

        User currentUser = authService.getCurrentUser(httpServletRequest);

        return UserInfo.of(
                currentUser.getUserId(),
                currentUser.getProviderId(),
                currentUser.getRole(),
                currentUser.getEmail(),
                currentUser.getName(),
                currentUser.getImage()
        );
    }

}
