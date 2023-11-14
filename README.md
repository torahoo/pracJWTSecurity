[2023-11-14]
JAVA 11
spring boot 2.7.1
MY SQL
Mustache

Spring Security 프로젝트를 해보고 JWT를 이용한 새 프로젝트 진행

[2023-11-14]
user, manager, admin 권한 설정을 통해 각 ROLE에 맞는 페이지 제한 설정

 - SecurityConfig
   - @EnableWebSecurity : 활성화를 위한 어노테이션
     스프링 시큐리티 필터가 스프링 필터체인에 등록 됨.
   - 설정 이후 login 페이지가 시큐리티에 의해 낚아채지지 않음
   - .and()
     .formLogin()
     .loginPage("/login");
     위 세팅을 permitAll() 다음에 붙여주고 나니
     권한이 필요한 다른 페이지로 이동 시도시에 login 페이지로 자동 이동 됨.

 - WebMvcConfig
   - 주의 점 : Mustache 임포트 시 아래 주소 꼭 확인
     import org.springframework.boot.web.servlet.view.MustacheViewResolver;


[2023-11-14] ERROR CODE

 - Access to DialectResolutionInfo cannot be null when 'hibernate.dialect' not set
   - database: mysql
     database-platform: org.hibernate.dialect.MySQL8InnoDBDialect
     application.yml에 위 구문 추가. 
   - 이 오류는 MySQL을 처리해줄수 있는 dialect의 값이 제대로 설정되지 않아서 나타나는 오류
   - JPA에 값을 지정해주면 된다.

 - templates에 있는 인덱스 페이지 보이지 않는 오류
   - https://bottom-to-top.tistory.com/38
   - yml 세팅
   - 해결 안됨.
   - config를 통해 세팅 ==> viewResolver를 통해 세팅
