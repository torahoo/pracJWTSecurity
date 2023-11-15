[2023-11-14]
JAVA 11
spring boot 2.7.1
MY SQL
Mustache

Spring Security 프로젝트를 해보고 JWT를 이용한 새 프로젝트 진행
강의 : https://www.youtube.com/playlist?list=PL93mKxaRDidERCyMaobSLkvSPzYtIk0Ah

 - 환경설정 [2023-11-14]
 - 시큐리티 설정 [2023-11-14]
 - 시큐리티 회원가입 [2023-11-14]
 - 시큐리티 로그인 [2023-11-15]
 - 시큐리티 권한 처리
 - 구글 로그인 준비
 - 구글 회원 프로필 정보 받아보기
 - Authentication 객체가 가질수 있는 2가지 타입
 - 구글 로그인 및 자동 회원가입 진행 완료
 - 페이스북 로그인 완료
 - 네이버 로그인 완료
 - JWT를 이해하기 전 세션에 대해 알아보자
 - JWT를 이해하기전 TCP에 대해서 알아보자
 - JWT를 이해하기전 CIA에 대해서 알아보자
 - JWT를 이해하기전 RSA에 대해서 알아보자
 - JWT를 이해하기전 RFC문서란
 - JWT 구조 이해
 - JWT 프로젝트 세팅
 - JWT를 위한 yml파일 세팅
 - JWT를 위한 security 설정
 - JWT Bearer 인증 방식
 - JWT Filter 등록 테스트
 - JWT 임시 토큰 만들어서 테스트
 - JWT를 위한 로그인 시도
 - 회원가입 및 JWT를 위한 강제 로그인 진행
 - JWT 토큰 만들어서 응답하기
 - JWT 토큰 서버 구축 완료

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

 - Model
   - User
     - Timestamp는 java.sql이 갖고있는걸 쓰면 된다.

[2023-11-15] 

    


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
