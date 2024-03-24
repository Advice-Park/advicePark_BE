package com.mini.advice_park.domain.oauth2;

import com.mini.advice_park.domain.oauth2.domain.OAuth2Provider;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.user.service.UserDeleteService;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RevokeService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserDeleteService userDeleteService;


    /**
     * 구글 계정 연결 해제
     */
    @Transactional
    public BaseResponse<Void> deleteGoogleAccount(String accessToken) {
        try {
            User user = extractUserFromAccessToken(accessToken);
            userDeleteService.deleteUserAccount(user);

            String data = "token=" + jwtUtil.getAuthentication(accessToken).getPrincipal();
            sendRevokeRequest(data, OAuth2Provider.GOOGLE, null);

            return new BaseResponse<>(HttpStatus.OK.value(), "구글 계정 연결 해제 성공", null);

        } catch (Exception e) {
            log.error("Error deleting Google account: {}", e.getMessage());
            return new BaseResponse<>(ErrorCode.INTERNAL_SERVER_ERROR.getStatus(), "구글 계정 연결 해제 실패", null);
        }
    }

    /**
     * 네이버 계정 연결 해제
     */
    @Transactional
    public BaseResponse<Void> deleteNaverAccount(String accessToken) {
        try {
            User user = extractUserFromAccessToken(accessToken);
            userDeleteService.deleteUserAccount(user);

            String data = "client_id=" + naverClientId +
                    "&client_secret=" + naverClientSecret +
                    "&access_token=" + jwtUtil.getAuthentication(accessToken).getPrincipal() +
                    "&service_provider=NAVER" +
                    "&grant_type=delete";

            sendRevokeRequest(data, OAuth2Provider.NAVER, null);

            return new BaseResponse<>(HttpStatus.OK.value(), "네이버 계정 연결 해제 성공", null);

        } catch (Exception e) {
            log.error("Error deleting Naver account: {}", e.getMessage());
            return new BaseResponse<>(ErrorCode.INTERNAL_SERVER_ERROR.getStatus(), "네이버 계정 연결 해제 실패", null);
        }
    }

    /**
     * 카카오 계정 연결 해제
     */
    @Transactional
    public BaseResponse<Void> deleteKakaoAccount(String accessToken) {
        try {
            User user = extractUserFromAccessToken(accessToken);
            userDeleteService.deleteUserAccount(user);
            sendRevokeRequest(null, OAuth2Provider.KAKAO, accessToken);

            return new BaseResponse<>(HttpStatus.OK.value(), "카카오 계정 연결 해제 성공", null);

        } catch (Exception e) {
            log.error("Error deleting Kakao account: {}", e.getMessage());
            return new BaseResponse<>(ErrorCode.INTERNAL_SERVER_ERROR.getStatus(), "카카오 계정 연결 해제 실패", null);
        }
    }

    /**
     * 액세스 토큰을 통해 사용자 정보 추출
     */
    private User extractUserFromAccessToken(String accessToken) {
        String email = jwtUtil.getEmail(accessToken);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ERROR));
    }

    /**
     * 액세스 토큰 폐기 요청
     */
    private void sendRevokeRequest(String data, OAuth2Provider provider, String accessToken) {

        String googleRevokeUrl = "https://accounts.google.com/o/oauth2/revoke";
        String naverRevokeUrl = "https://nid.naver.com/oauth2.0/token";
        String kakaoRevokeUrl = "https://kapi.kakao.com/v1/user/unlink";

        RestTemplate restTemplate = new RestTemplate();
        String revokeUrl = "";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(data, headers);

        switch (provider) {
            case GOOGLE:
                revokeUrl = googleRevokeUrl;
                break;
            case NAVER:
                revokeUrl = naverRevokeUrl;
                break;
            case KAKAO:
                revokeUrl = kakaoRevokeUrl;
                headers.setBearerAuth(accessToken);
                break;
        }

        restTemplate.exchange(revokeUrl, HttpMethod.POST, entity, String.class);
    }

}
