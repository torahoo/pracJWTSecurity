package com.pracjwtsecurity.config.auth;

/*
=============================================================
시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
로그인 진행이 완료되면 시큐리티 session을 만들어줍니다. (Security ContextHolder)
오브젝트 => Authentication 타입 객체
Authentication 안에 User 정보가 있어야 함.
User 오브젝트 타입 => UserDetails 타입 객체

Security Session => Authentication => UserDetails(PrincipalDetails) (같은 타입이 됨)
=============================================================
 */

import com.pracjwtsecurity.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user; //콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    /*
    해당 User의 권한을 리턴
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        /*
        한 사이트에서 회원이 1년동안 로그인을 하지 않았을때 비활성화
        user.getLoginDate();를 가져와서 확인
        현재시간 - 로그인 시간 => 1년을 초과하면 return false
         */
        return true;
    }
}
