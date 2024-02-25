package com.mini.advice_park.service;

import com.mini.advice_park.domain.User;
import com.mini.advice_park.dto.auth.AuthLoginDto;
import com.mini.advice_park.dto.auth.AuthSignUpDto;
import com.mini.advice_park.dto.jwt.JwtTokenDto;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.global.security.jwt.JwtProvider;
import com.mini.advice_park.repo.UserRepository;
import com.mini.advice_park.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Boolean signUp(AuthSignUpDto authSignUpDto) {
        userRepository.save(
                User.signUp(
                        authSignUpDto,
                        bCryptPasswordEncoder.encode(authSignUpDto.password())
                )
        );
        return true;
    }

    @Transactional
    public JwtTokenDto login(AuthLoginDto authLoginDto) {
        User user = userRepository.findBySocialId(authLoginDto.socialId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!bCryptPasswordEncoder.matches(authLoginDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCHED_USER);
        }

        JwtTokenDto jwtToken = jwtProvider.createTokens(user.getSocialId(), user.getUserType());
        user.updateRefreshToken(jwtToken.refreshToken());
        return jwtToken;
    }

    @Transactional
    public Boolean logout(String socialId) {
        User user = userRepository.findBySocialIdAndIsLogin(socialId, true)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        user.logout();
        return true;
    }

    @Transactional
    public JwtTokenDto reissue(String socialId, String refreshToken) {
        User user = userRepository.findBySocialIdAndRefreshTokenAndIsLogin(socialId, refreshToken, true)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ERROR));
        JwtTokenDto jwtTokenDto = jwtUtil.generateTokens(user.getSocialId(), user.getUserType());
        user.updateRefreshToken(jwtTokenDto.refreshToken());

        return jwtTokenDto;
    }
}
