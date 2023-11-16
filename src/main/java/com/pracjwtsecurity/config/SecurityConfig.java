package com.pracjwtsecurity.config;

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
=================================================================================
 */

@Configuration
@EnableWebSecurity // 활성화를 위한 어노테이션
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .defaultSuccessUrl("/");
    }
}
