package com.pracjwtsecurity.auth;

import com.pracjwtsecurity.model.User;
import com.pracjwtsecurity.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
====================================================================
시큐리티 설정에서 loginProcessingUrl("/login");
/login 요청이 오면 UserDetailsService 타입으로
IoC되어있는 loadUserByUsername 함수가 실행
====================================================================
 */

@Log4j2
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /*
    시큐리티 session = Authentication = UserDetails
    시큐리티 session(내부 Authentication(내부 UserDetails))
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("login username = "+username );
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
