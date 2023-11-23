package com.pracjwtsecurity.config.oauth;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    /*
    구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest getClientRegistration:" + userRequest.getClientRegistration());
        log.info("userRequest getAccessToken:" + userRequest.getAccessToken());
        log.info("super.loadUser(userRequest).getAttributes():" + super.loadUser(userRequest).getAttributes());

        // Attribute 정보를 토대로 회원가입을 강제로 진행
        return super.loadUser(userRequest);
    }
}
