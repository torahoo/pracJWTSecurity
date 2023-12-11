package com.pracjwtsecurity.config.oauth;

import com.pracjwtsecurity.config.auth.PrincipalDetails;
import com.pracjwtsecurity.config.oauth.provider.FacebookUserInfo;
import com.pracjwtsecurity.config.oauth.provider.GoogleUserInfo;
import com.pracjwtsecurity.config.oauth.provider.NaverUserInfo;
import com.pracjwtsecurity.config.oauth.provider.OAuth2UserInfo;
import com.pracjwtsecurity.model.User;
import com.pracjwtsecurity.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log4j2
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    /*
    구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // registrationId로 어떤 OAuth로 로그인 햇는지 확인 가능
        log.info("userRequest getClientRegistration:" + userRequest.getClientRegistration());
        log.info("userRequest getAccessToken:" + userRequest.getAccessToken());

        /*
        구글 로그인 버튼 클릭 ==> 구글 로그인 창 ==> 로그인을 완료 ==> code를 리턴 (OAuth-Client라이브러리) ==> AccessToken 요청
        userRequest 정보 ==> loadUser함수 호출 ==> 구글로부터 회원 프로필 받아준다.
         */
        log.info("super.loadUser(userRequest).getAttributes():" + super.loadUser(userRequest).getAttributes());
        OAuth2User oauth2User = super.loadUser(userRequest);
        log.info("PrincipalOAuth2User oauth2User getAttributes : "+oauth2User.getAttributes());

        //강제로 회원가입을 진행해볼 예정
        /* ======================================================================== *
            Google과 Facebook에 따라 providerId로 불러올 값이 다르기에 이를 구분해준다.
         * ======================================================================== */
        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            log.info("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.info("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
        } else {
            log.info("구글과 페이스북과 네이버만 지원함");
        }
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider+"_"+providerId; // google_google subId
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";


        /* ======================================================================== *
            위에서 구분을 하고 get으로 값을 가져올 수 있기에 밑은 주석처리한다.
         * ======================================================================== */
//        String provider = userRequest.getClientRegistration().getClientName(); //google
//        String providerId = oauth2User.getAttribute("sub"); //google subId
//        String username = provider+"_"+providerId; // google_google subId
//        String password = bCryptPasswordEncoder.encode("겟인데어");
//        String email = oauth2User.getAttribute("email");
//        String role = "ROLE_USER";

        /*
        ===========================================================
        구글 로그인을 통해 회원가입 할 시 이미 회원가입이 되어있는 아이디는
        걸러주기 위한 userRepository.findByUsername(username)
        ===========================================================
         */
        User userEntity = userRepository.findByUsername(username);
        if(userEntity==null) {
            log.info("최초 OAuth 로그인");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            User oauth2UserSave = userRepository.save(userEntity);
        } else {
            log.info("이미 존재하는 회원 입니다.");
        }


        // Attribute 정보를 토대로 회원가입을 강제로 진행
//        return super.loadUser(userRequest);
        //OAuth 강제 회원가입을 위한 return 변경
        return new PrincipalDetails(userEntity, oauth2User.getAttributes()); //==> Authentication 객체에 들어감

    }

}
