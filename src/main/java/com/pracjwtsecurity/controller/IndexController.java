package com.pracjwtsecurity.controller;

import com.pracjwtsecurity.model.User;
import com.pracjwtsecurity.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"","/"})
    public String index() {
        log.info("index Page Open");
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    /*  ==========================================
        /login
        스프링 시큐리티가 해당 주소를 낚아채 리턴값이 안보임
        ==> SecurityConfig 파일 생성 후 작동 안함
        ==========================================
     */
    @GetMapping("/loginForm")
    public String loginForm(){
        log.info("loginFormPage Open");
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        log.info(user.getUsername()+"님 회원가입 완료");
        user.setRole("ROLE_USER");
//        User save01 = userRepository.save(user); // ==> 패스워드 암호화가 안되었기에 시큐리티로 로그인을 할 수 없음

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);

        return "redirect:/loginForm";
    }

}
