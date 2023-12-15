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
 - 페이스북 로그인 완료 [2023-11-28] 8:08 까지 진행 / [2023-11-29]
 - 네이버 로그인 완료 [2023-12-11]
 - JWT를 이해하기 전 세션에 대해 알아보자 [2023-12-15]
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

[2023-11-29]
 - 페이스북 로그인 완료
 - 로그인 시 찍히는 값
   - sub = null ==> attribute에서 페이스북은 sub가 아닌 id에 값이 들어가있어 sub는 null값이 나온다.
   - (* 강의에선 provider로 userRequest.getClientRegistration().getClientId(); 를
        사용하였지만 getClientName으로 해야 google이란 값이 나온다. *)

 - oauth 패키지 안에 provider 패키지 생성
 - OAuth2UserInfo interface 생성
   - String getProviderId(); ==> sub
     String getProvider(); ==> google / facebook
     String getEmail(); ==> 회원 이메일
     String getName(); ==> 회원 이름
 - GoogleUserInfo 클래스 생성 후 OAuth2UserInfo implements
 - 모든 메서드 override하고 생성자 생성
 - 오버라이드 된 메서드 return값 각 값에 맞게 설정
 - GoogleUserInfo 복사해서 Facebook으로 변경
 - providerId return값 "sub" 에서 "id"로 변경
 - provider "google" ==> "facebook"
 - PrincipalOauth2UserService에서 두 로그인을 구분해줄 if문 추가
 - 해당 값에 맞게 끔 변수들을 get으로 바꿔줌.

[2023-12-11]
 - 네이버 로그인 완료
 - application.yml에 네이버를 위한 세팅
 - 스프링 부트 oauth2 code client (참고 : https://blog.naver.com/getinthere/222064999924 )
   - 스프링부트 시큐리티 OAuth2.0 (토큰방식)
     - Facebook, Google, Naver 세션 방식
     - 코드를 부여하는 방식 (Authorization Code Grant Type)
 - client-id / client-secret까진 동일
 - scope : - name, - email ( - profile_image 란 것이 있지만 지금은 쓰지 않으므로 넣지 않음)
 - client-name : Naver
 - authorization-grant-type : authorization_code (==> 코드를 부여하는 방식에 의한 type 지정)
 - redirect-uri: http://localhost:9000/login/oauth2/code/naver
   (http://localhost:9000/login/oauth2/code 여기까진 동일)
   ==> 구글, 페이스북은 자동으로 해주기에 redirect-uri 넣어줄 필요 없음 
   ==> 네이버는 provider로 기본 세팅이 안되었기에 직접 명시해줘야 함.
 - 네이버로부터 정보 받아오기. (id, secret 등등...)
   - 네이버 개발자 센터 들어가기
   - application --> application 등록
   - 애플리케이션 등록 (API 이용신청) --> 애플리케이션 이름 등록 (pracJWTSecurity)
   - 사용 API 네이버 로그인 선택 후 이메일, 회원이름 선택
   - 로그인 오픈 API 서비스 환경 세팅 (PC웹)
     - 서비스 URL --> http://localhost:9000
     - 네이버 아이디로 로그인 Callback URL --> http://localhost:9000/login/oauth2/code/naver
   - 등록하기
   - client-id / client-secret 가져오기.
   - loginForm에 네이버 로그인 추가
   - securityConfig에서 naver가 없기에 Error creating bean 에러가 뜬다.
   - application.yml에 provider 구문 추가하여 해결 (에러 코드쪽 참고)
   - Missing attribute 'response' in attributes 에러 발생 (에러 코드쪽 참고)
   - PrincipalOauth2UserService에 네이버 구분하는 if문 추가 및 NaverUserInfo 생성
   - naver response :
     { resultcode=00,
     message=success,
     response={id=네이버ID, email=legokbs@naver.com, name=김태윤}}
     로 나옴.
   - PrincipalOauth2UserService에 Naver로 회원가입 진행
     - oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"))
     - ==> Database의 USER에 값 제대로 들어오는것 확인
     - reponse에 회원정보가 NaverUserInfo에 attributes로 들어가 값을 알아서 뽑아줌.
     - url /user로 진입 
       ==> principalDetails : 
            User(
            id=12, 
            username=naver_Jw7wJ7LC_9p6Peg0_pm3LHaehzSY7Zoxc28Odou9XGQ, 
            password=$2a$10$PP2DMF3zcp97mLeYNXZGlO.HcvYptdAXiPJ1Q3MVuAmDiYv5QKKWy, 
            email=legokbs@gmail.com, 
            role=ROLE_USER, 
            provider=naver, 
            providerId=Jw7wJ7LC_9p6Peg0_pm3LHaehzSY7Zoxc28Odou9XGQ, 
            createDate=2023-12-11 18:29:41.448
            )
     - 정리 : 
     스프링부트 기본 로그인 + OAuth2.0 로그인 = 통합해서 구현
     웹 애플리케이션 만들면 됨.

 [2023-12-15]
 - JWT를 이해하기 전 세션에 대해 알아보자
 - JWT (JSON Web Token) : 
 왜? 
 어디에?
 - 세션
   - HTTP 헤더
     - 쿠키 : 세션ID - 1234
   - 웹 브라우저에서 세션ID를 받아 쿠키 저장
 - 세션값이 사라지는 경우
   - 서버에서 세션 값을 지울 시
   - 사용자가 브라우저를 종료 시
   - 특정 시간(DEFAULT 30분)이 지날 시
 - 세션이 생기는 시점
   - 로그인 요청(인증) 시
     - (1) : 클라이언트(사용자)가 최초 request를 하면 
     - (2) : 서버에서 세션ID를 생성해 부여한다.
     - (3) : 응답(response)할 때 헤더에 세션ID를 돌려준다.
     - (4) : 클라이언트한테 세션ID가 저장.
     - (5) : 로그인 요청 ID:비밀번호 서버로 값 보냄
     - (6) : DB 조회
     - (7) : 정상일시 세션ID에 user정보를 저장, 메인페이지.html로 보냄
     - (8) : 클라이언트가 유저 정보를 요청함
     - (9) : 서버에서 세션ID를 찾음
     - (10) : DB에서 유저 정보를 응답받아
     - (11) : 클라이언트로 반환

 - 세션의 단점
   - 클라이언트가 서버로 request할 때 동접자 수가 많으면
     (서버의 동접자 처리 가능 수가 100명인데 동접자가 300명이 넘어갈 시)
     여러 서버를 이용해 로드 밸런싱을 할 수 있다. (100명 처리가능한 서버가 여러개 있어
     부하를 분산 처리하는 방법)
   - 위의 경우 서버에서 세션을 저장하고 response한 뒤,
     클라이언트가 다시 요청을 하면 세션을 저장했던 서버에 요청이가면 상관이 없지만
     해당 서버에 부하가 많아 다른 서버로 request가 가면 저장된 세션이 없어 오류가 발생한다.
     (이때마다 세션을 복제해 다른 서버에도 부여할 수 있지만 쉽지 않음)
   - 세션값을 공용 DB에 저장하고 쓰면 이 문제를 해결할 수 있지만 I/O Exception이 발생한다.
   - 값이 HDD에 있어 그 값을 꺼내올때 RAM에 저장하여 사용되어지면 다음 그 값을 쓸 때는 RAM에서
     처리하여 I/O가 발생하지 않지만 위의 경우처럼 DB에 세션값을 넣으면 요청할 때마다 HDD에서 값을
     가져와야 함으로 I/O가 발생된다.

 - 이때 JWT를 사용하는 이유는?
   - 세션의 고질적인 문제점을 해결하기 위해 사용한다.

[이하 JWT와 관련된 자세한 내용은 서술하지 않고 넘어간다.] --> 17강으로 넘어감.





========================================================================================
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

[2023-11-29]
 - 페이스북 로그인 시 
 - Sorry, something went wrong.
   We're working on getting this fixed as soon as we can.
   라는 문구와 함께 오류 발생
 - 추측 1 : 페이스북의 아이디 패스워드가 틀린것 같다.
   - 해결 : https://www.inflearn.com/questions/1049529/facebook-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EB%B2%84%ED%8A%BC%EB%A7%8C%EB%93%A0-%ED%9B%84-error-sorry-%EB%B0%9C%EC%83%9D%EC%8B%9C
   해당 사이트에서 해결방법을 찾음. 결국 인증 및 계정 만들기 ==> 수정 에서 Email 권한 추가해주어야 오류가 나지 않는다.

[2023-12-11]
 - UnsatisfiedDependencyException
   - 네이버 로그인을 추가하면서 securityConfig에 네이버 정보가 없기에 생기는 오류
   - 해결 : 
     - application.yml에 provider 구문 추가
       - provider:
           naver:
           authorization-uri: http://nid.naver.com/oauth2.0/authorize
           token-uri: https://nid.naver.com/oauth2.0/token
           user-info-uri: https://openapi.naver.com/v1/nid/me
           user-name-attribute: response #회원 정보를 json으로 받는데 response라는 키값으로 네이버가 리턴해줌.

 - Missing attribute 'response' in attributes 에러 발생
   - PrincipalOauth2UserService에서 oauth2User.getAttributes() 호출이 불가능해서 생기는 오류
   - reponse라는 키값안에 정보가 들어있기에 이를 잘 써야함. 현재는 적용 안되있기에 nullPointException 발생
   - PrincipalOauth2UserService에 코드 추가
   - 해결 : 
     - if 문으로 구글, 페이스북 구분하던 라인에 네이버 추가
     - NaverUserInfo 생성
     - PrincipaOauth2UserService에 다른 두개와는 달리 getAttributes로 꺼내는게 아니라
       (Map)oauth2User.getAttributes().get("response") 로 값을 꺼내야 response 안의 
       사용자 정보들을 가져올 수 있다.