package com.pracjwtsecurity.config.oauth.provider;

import java.util.Map;

/* ================================================= *
Naver는 response에 값을 가지고 있기에 구글과 페이스북과는 달리
response 반환값을 주어야 한다.
naver getAttributes : resultcode / message / response (id, email, name)
 * ================================================= */

public class NaverUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes; // oauth2User.getAttributes();

    // response = {id=loginUserNaverID, email=loginUserEmail, name=loginUserName}
    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
