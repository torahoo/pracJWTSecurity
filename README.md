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
 - Authentication 객체가 가질수 있는 2가지 타입 [2023-11-23] 20:17 까지 진행 / [2023-11-24]
 - 구글 로그인 및 자동 회원가입 진행 완료 [2023-11-24]
 - 페이스북 로그인 완료 [2023-11-28] 8:08 까지 진행
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

 - Authentication객체가 가질수 잇는 2가지 타입
   - registrationId로 어떤 OAuth로 로그인 햇는지 확인 가능 : userRequest.getClientRegistration()
   - 구글 로그인 버튼 클릭 ==> 구글 로그인 창 ==> 로그인을 완료 ==> code를 리턴 (OAuth-Client라이브러리) ==> AccessToken 요청
     userRequest 정보 ==> loadUser함수 호출 ==> 구글로부터 회원 프로필 받아준다.
     - super.loadUser(userRequest).getAttributes()
   - PrincipalOauth2UserService에 OAuth2User oauth2User = super.loadUser(userRequest); 추가
   - IndexContorller에 세션 정보를 확인하는 메서드 추가
     @GetMapping("/test/login")
     public @ResponseBody String loginTest(Authentication authentication) {
     log.info("/test/login ==============================");
     log.info("authentication : "+authentication.getPrincipal());
     return "세션 정보 확인하기";
     }
   - result :
     /test/login ==============================
     authentication : com.pracjwtsecurity.config.auth.PrincipalDetails@53f1e8b6
   - 메서드에 PrincipalDetails 추가
   - 밑의 코드로 메서드 바꾸기
     log.info("/test/login ==============================");
     PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
     log.info("authentication : "+principalDetails.getUser());
   - result :
     /test/login ==============================
     authentication : 
     User(id=1, 
          username=user, 
          password=$2a$10$MpkAfJkTOjW9V6jJ8MyJ2uc5m5L1LrNPWbOLKPNWbEkXmdHFQ67nm, 
          email=user@test.com, 
          role=ROLE_USER, 
          provider=null, 
          providerId=null, 
          createDate=2023-11-16 17:01:15.519)
   - Authentication authentication ==> DI(의존성 주입)
   - 메서드에 @AuthenticationPrincipal 추가
   - @AuthenticationPrincipal UserDetails userDetails를 이용해 user 정보를 가져올 수 있다.
     userDetails : user ==> 이 유저는 UserDetails 타입
   - UserDetails 타입이 아닌 PrincipalDetails타입으로도 정보를 가져올 수 있다.
     ==> PrincipalDetails로는 getUsername이 아닌 getUser가 가능해진다.
   result : userDetails2 : User(id=1, 
                                username=user, 
                                password=$2a$10$MpkAfJkTOjW9V6jJ8MyJ2uc5m5L1LrNPWbOLKPNWbEkXmdHFQ67nm, 
                                email=user@test.com, 
                                role=ROLE_USER, 
                                provider=null, 
                                providerId=null, 
                                createDate=2023-11-16 17:01:15.519)

[2023-11-24]
 - Authentication 객체가 가질수 있는 2가지 타입
 - 스프링 시큐리티 
   - 시큐리티 세션 ==> Dependency Injection (DI)
     - Authentication
       - UserDetails ==> 일반 로그인
       - OAuth2User ==> OAuth 로그인 (구글, 페이스북, 카카오 등등...)

 - 일반적인 로그인 세션에 접근하기 위해선
   @AuthenticationPrincipal PrincipalDetails(/UserDetails) userDetails (==> 일반 로그인)
   @AuthenticationPrincipal OAuth2User oauth (==> 구글, 페이스북 등의 로그인)
   로 객체를 받아와 써야한다.
 - Controller 에서 각각의 로그인 타입마다 따로 써줘야한다.
 - X라는 클래스를 만들어 UserDetails와 OAuth2User를 implements 받을 경우 X클래스를 이용하여 
   두 객체를 다 쓸 수 있게 된다. ==> PrinciapDetails(userEntity) 타입으로 return 받으면 된다.
 - ==> 결국 OAuth2User또한 PrincipalDetails로 같이 엮어버리면 둘다 편히 쓸 수 있다.
 - 정리 : 어떤 로그인 타입이냐에 따라 Controller에서 로그인 정보를 꺼내기 위해서 다른 타입을
         써야하기에 불편함이 생기므로 기존 UserDetails타입을 PrincipalDetails로 썼던 것 처럼
         OAuth2User 또한 implements하여 모든 로그인 정보를 PrincipalDetails로 꺼내 쓸 수 있게 한다.

 - 구글 로그인 및 자동 회원가입 진행 완료
   - PrincipalDetails 를 만든 이유
     - Security가 가지고 있는 세션 정보 Authentication 객체 정보를 꺼내오기 위해
       - OAuth2User 타입 ==> PrincipalDetails로 같이 엮어 일반 로그인, 구글 로그인 전부 여기서 꺼내기로 함.
       - UserDetails 타입 ==> PrincipalDetails가 정보를 가지고 있어 이걸로 User정보를 꺼내면 됨.
   - PrincipalOAuth2UserService에 강제로 회원가입을 진행하기
     - 비밀번호 저장을 위해 
       @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder; 추가
   - User엔티티에 Builder 패턴을 통해 생성자 주입
   - 해당 생성자를 통해 DB에 회원이 존재하는지 찾고 없으면 회원가입 진행
   - return을 PricipalDetails로 바꿈. 
     ==> OAuth, 일반 로그인 전부 PrincipalDetails 객체로 받을 수 있기에 Authentication으로 값을 꺼낼 수 있음.

 - result : 
 - 일반 로그인의 경우 : 
   - principalDetails : 
        User(   id=1, 
                username=user, 
                password=$2a$10$MpkAfJkTOjW9V6jJ8MyJ2uc5m5L1LrNPWbOLKPNWbEkXmdHFQ67nm, 
                email=user@test.com, 
                role=ROLE_USER, 
                provider=null, 
                providerId=null, createDate=2023-11-16 17:01:15.519)
   - OAuth 로그인의 경우 :
     principalDetails : 
        User(   id=4, 
                username=722336461687-otmm8jhfrvdfjbkiio09p2tpu0q55lj2.apps.googleusercontent.com_111128189196468219361, 
                password=$2a$10$CuyYZ3hFXeQr8rgWiXkqi.K9w5FZMg9UlGcRBRvf/TjOrInihX9WW, 
                email=legokbs@gmail.com, 
                role=ROLE_USER, 
                provider=722336461687-otmm8jhfrvdfjbkiio09p2tpu0q55lj2.apps.googleusercontent.com, 
                providerId=111128189196468219361, 
                createDate=2023-11-24 17:10:06.28)

 - User 정보를 꺼내오기 위해선 (UserDetails) 처럼 다운 캐스팅이 필요하지만 @AuthenticationPrincipal를 이용하면
   바로 꺼내올 수 있다.
   ==> PrincipalOauth2UserService와 PrincipalDetailsService를 만든 이유
       - 그냥으로도 loadUser와 loadUserByUsername은 작동하지만 궂이 만든 이유는
         PrincipalDetails로 return하여 값을 받기 위함이다.



[2023-11-28]
 - 페이스북 로그인 완료

 - 페이스북 api 콘솔 접속
 - 페이스북 로그인 후 내 앱 접속
 - 앱 만들기로 앱 ID 생성
 - 대시보드에서 이용사례 선택 하고 인증 및 계정 만들기 맞춤설정 선택, 빠른 시작 페이지로 이동 접속
 - 웹 선택
 - 사이트 URL 저장 (http://localhost:{설정 포트 번호})
 - 완료 후 대시보드의 기본 설정 페이지로 이동
 - application.yml 설정
 - google과 똑같이 세팅
 - 페이스북 앱ID 계정의 앱 ID와 시크릿 코드 가져오기 (profile ==> public_profile)
 - scope 확인 방법
   문서 - 페이스북 로그인 - 웹 - 사용자 로그인 (호출 샘플)

 - loginForm에 페이스북 로그인 버튼 추가
   (/oauth2/authorization/facebook 이렇게만 추가해도 라이브러리가 알아서 처리해 줌)

ERROR CODE

[2023-11-14] 

 - Access to DialectResolutionInfo cannot be null when 'hibernate.dialect' not set
   - database: mysql
     database-platform: org.hibernate.dialect.MySQL8InnoDBDialect
     application.yml에 위 구문 추가. 
   - 이 오류는 MySQL을 처리해줄수 있는 dialect의 값이 제대로 설정되지 않아서 나타나는 오류
     JPA에 값을 지정해주면 된다. 

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

 - class org.springframework.security.oauth2.core.user.DefaultOAuth2User 
   cannot be cast to class com.pracjwtsecurity.config.auth.PrincipalDetails 
   (org.springframework.security.oauth2.core.user.DefaultOAuth2User is in unnamed module 
   of loader 'app'; com.pracjwtsecurity.config.auth.PrincipalDetails is in unnamed module 
   of loader org.springframework.boot.devtools.restart.classloader.RestartClassLoader @11838285)
 - Class Cast ERROR (ClassCastException)
 - IndexController / loginTest 메서드에서 userDetails객체가 PrincipalDetails로 캐스팅 되지 않아 생기는 오류
   - 해결 :
     PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
     위의 코드에서 Google로그인 시 OAuth로 인증하여 들어오는 로그인 이기에 PrincipalDetails로 캐스팅이 되지 않아
     발생하는 문제이므로
   - OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
     로 바꿔주도록 한다. ==> 정보를 꺼내 쓸때는 oAuth2User.getAttributes()를 사용한다.
   - @AuthenticationPrincipal OAuth2User oauth 를 통해 oauth.getAttribute()를 사용해도
     위의 정보와 동일하게 나온다.

[2023-11-24]
 - No default constructir for entity : com.pracjwtsecurity.model.User
   - 구글 로그인을 통해 강제 회원가입을 진행하던 중 @Builder 패턴을 이용해 생성자를 만들어
     생긴 Error
   - 해결 : User 엔티티에 @NoArgsConstructor 넣음으로서 해결
