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
 - 시큐리티 권한 처리 [2023-11-16]
 - 구글 로그인 준비 [2023-11-16]
 - 구글 회원 프로필 정보 받아보기 [2023-11-23]
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
시큐리티 로그인

[2023-11-16]
시큐리티 권한처리

구글 로그인 준비
 - outh와 관련되어 궁금한게 있으면 이전 강의 참고
 - Google API console
 - https://console.cloud.google.com/apis/dashboard?hl=ko&project=pracjwtsecurity&supportedpurview=project
 - google api를 위한 새 프로젝트 생성
 - 승인된 리디렉션 URI 설정 (http://localhost:9000/login/oauth2/code/google) : 사용자 인증 정보에서 진행
 - Access 토큰을 얻기 위한 주소 (사용자의 민감한 정보등을 열람 혹은 가져올 수 있음)
 - http://localhost:9000/login/oauth2/code 까지는 주소가 고정.
   그 뒤에 붙이는 건 google이면 구글, facebook이면 페이스북 처럼 필요에 의해 붙인다.
 - indexController에 따로 주소를 추가할 필요는 없다.
 - 라이브러리 설치 : OAuth2 라이브러리 (OAuth-client)
 - https://docs.spring.io/spring-security-oauth2-boot/docs/2.1.7.RELEASE/reference/html5/
 - compile 'org.springframework.boot:spring-boot-starter-security'
   compile 'org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.1.7.RELEASE'

[2023-11-23]
 - 구글 로그인이 완료된 뒤의 후처리가 필요함.
   1. 코드받기(인증 했다는 말) 
   2. 엑세스 토큰(권한 부여) 
   3. 사용자프로필 정보를 가져옴 
   4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
   4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소) /
        백화점몰 -> (vip등급, 일반등급)
        ==> 서비스 이용시 구글 로그인으로 가져오는 정보만으로 부족할 시
        회원가입을 따로 진행시켜 필요한 정보를 더 받아야 한다.
        구글 로그인 완료시 코드X (엑세스토큰 + 사용자프로필 정보O)

 - log.info("userRequest getClientRegistration:" + userRequest.getClientRegistration());
   - userRequest getClientRegistration:
   ClientRegistration{registrationId='google', 
   clientId='722336461687-otmm8jhfrvdfjbkiio09p2tpu0q55lj2.apps.googleusercontent.com', 
   clientSecret='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', 
   clientAuthenticationMethod=org.springframework.security.oauth2.core.ClientAuthenticationMethod@4fcef9d3, 
   authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3, 
   redirectUri='{baseUrl}/{action}/oauth2/code/{registrationId}', 
   scopes=[email, profile], 
   providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@27e1234d, 
   clientName='Google'}
 - log.info("userRequest getAccessToken:" + userRequest.getAccessToken());
   - userRequest getAccessToken:org.springframework.security.oauth2.core.OAuth2AccessToken@fb049c5c
 - log.info("super.loadUser(userRequest).getAttributes():" + super.loadUser(userRequest).getAttributes());
   - super.loadUser(userRequest).getAttributes():
   {sub=111128189196468219361, 
   name=김일중, 
   given_name=일중, 
   family_name=김, 
   picture=https://lh3.googleusercontent.com/a/ACg8ocLw6akNUca1Q4MJrLJryBl5T_VWlun0FFuVYPnt4d7W=s96-c, 
   email=legokbs@gmail.com, 
   email_verified=true, locale=ko}
   - 로그인 시 사용할 정보 : 
     username = sub ==> "google_111128189196468219361",
     password = 암호화 ==> "암호화(겟인데어)"
     email = email ==> "legokbs@gmail.com"
     role = "ROLE_USER"
     provider = "google"
     prividerId = "111128189196468219361"


ERROR CODE

[2023-11-14] 

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

[2023-11-15] 

 - 로그인 안되는 에러 발생
   - 프론트에서 loginForm action이 아무것도 없었음.
   - action = '/login' method = 'post'
     를 붙여서 해결. SecurityConfig에서 /login url로 접속되면
     스프링 부트 로그인이 자동으로 로그인을 해줌.

[2023-11-23] 
 - SecurityConfig에 userService추가하고 ()안에 null값을 넣고 
   실행시키니 서버가 오류를 내보냈다.
 - Error creating bean with name 'springSecurityFilterChain' defined in class path resource
   - 해결 방법 : PrincipalOauth2UserService 클라스를 추가하여
               DefaultOAuth2UserService를 extends하여 
               SecurityConfig에
               @Autowired
               private PrincipalOauth2UserService principalOauth2UserService;
               하여 userService(principalOauth2UserService) 하면 해결한다.