package com.assu.cloud.authservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * 액세스 토큰에 추가 정보 삽입
 */
@Configuration
public class JWTTokenEnhancer implements TokenEnhancer {

    private String getUserId(String userName){
        // DB 로 유저 아이디 조회
        return "12345";
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        String userId =  getUserId(authentication.getName());

        additionalInfo.put("userId", userId);

        // 모든 추가 속성은 HashMap 에 추가하고, 메서드에 전달된 accessToken 변수에 추가
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
