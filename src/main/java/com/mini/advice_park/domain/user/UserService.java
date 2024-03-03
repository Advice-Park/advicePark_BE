package com.mini.advice_park.domain.user;

import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.user.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원가입 서비스
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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

}
