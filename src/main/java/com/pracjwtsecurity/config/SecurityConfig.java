package com.pracjwtsecurity.config;

import com.pracjwtsecurity.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
=================================================================================
@EnableGlobalMethodSecurity 어노테이션
=> securedEnabled=true ==> secure 어노테이션 활성화
=> prePostEnabled = true ==> preAutorize 어노테이션 활성화 / postAuthorize 어노테이션 활성화

글로벌로 권한을 걸고 싶을때는 configure로 authorizeRequests를 이용하여 세팅을 하면 되고
하나의 url에 권한을 걸고싶을때는 Controller에 어노테이션으로 secure 등을 이용하여 세팅하는 것을 권장.

구글 로그인이 완료된 뒤의 후처리가 필요함.
1. 코드받기(인증 했다는 말)
2.엑세스 토큰(권한 부여)
3.사용자프로필 정보를 가져옴
4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소) /
백화점몰 -> (vip등급, 일반등급)
==> 서비스 이용시 구글 로그인으로 가져오는 정보만으로 부족할 시
    회원가입을 따로 진행시켜 필요한 정보를 더 받아야 한다.
구글 로그인 완료시 코드X (엑세스토큰 + 사용자프로필 정보O)
=================================================================================
 */

@Configuration
@EnableWebSecurity // 활성화를 위한 어노테이션
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService); // 구글 로그인이 완료된 후의 후처리가 필요함.
    }
}
