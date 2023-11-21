package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.CEmailLoginFailedException;
import TABA4_9.CampShare.Entity.*;
import TABA4_9.CampShare.Repository.AccountRepository;
import TABA4_9.CampShare.Repository.RefreshTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository tokenRepository;
    private final SecurityService securityService;

    /* 환경변수 가져오기 */
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String KAKAO_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    String CLIIENT_SECRET;

    /* 인가코드로 kakaoAccessToken 따오는 메소드 */
    public KakaoTokenDto getKakaoAccessToken(String code) {

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
        params.add("client_id", KAKAO_CLIENT_ID); //카카오 앱 REST API 키
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("code", code); //인가 코드 요청시 받은 인가 코드값, 프론트에서 받아오는 그 코드
        params.add("client_secret", CLIIENT_SECRET);

        // 헤더와 바디 합치기 위해 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        System.out.println("[카카오 서버로 보낼 POST]");
        System.out.println(kakaoTokenRequest);

        // 카카오로부터 Access token 수신
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        System.out.println("카카오 서버로 code를 POST 전송 완료");
        System.out.println("[카카오 서버로부터 받아온 토큰]");
        System.out.println(accessTokenResponse);

        // JSON Parsing (-> KakaoTokenDto)
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoTokenDto kakaoTokenDto = null;
        try {
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("[object mapping 이후]");
        System.out.println(kakaoTokenDto);
        return kakaoTokenDto;
    }

    /* kakaoAccessToken 으로 카카오 서버에 정보 요청 */
    public Account getKakaoInfo(String kakaoAccessToken) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        // POST 방식으로 API 서버에 요청 보내고, response 받아옴
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        System.out.println("카카오 서버에서 정상적으로 데이터를 수신했습니다.");
        // JSON Parsing (-> kakaoAccountDto)
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoAccountDto kakaoAccountDto = null;
        try {
            kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
        } catch (JsonProcessingException e) { e.printStackTrace(); }

        // kakaoAccountDto 에서 필요한 정보 꺼내서 Account 객체로 매핑
        String email = kakaoAccountDto.getKakao_account().getEmail();
        String kakaoName = kakaoAccountDto.getKakao_account().getProfile().getNickname();

        return Account.builder()
                .loginType("KAKAO")
                .email(email)
                .kakaoName(kakaoName)
                .authority(Authority.ROLE_USER)
                .build();
    }

    /* login 요청 보내는 회원가입 유무 판단해 분기 처리 */
    public ResponseEntity<LoginResponseDto> kakaoLogin(String kakaoAccessToken) {
        // kakaoAccessToken 으로 회원정보 받아오기
        Account account = getKakaoInfo(kakaoAccessToken);
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setKakaoAccessToken(kakaoAccessToken);
        loginResponseDto.setAccount(account);
        try {
            TokenDto tokenDto = securityService.login(account.getEmail());
            loginResponseDto.setLoginSuccess(true);
            HttpHeaders headers = setTokenHeaders(tokenDto);
            return ResponseEntity.ok().headers(headers).body(loginResponseDto);
        } catch (CEmailLoginFailedException e) {
            loginResponseDto.setLoginSuccess(false);
            return ResponseEntity.ok(loginResponseDto);
        }
    }

    /* 토큰을 헤더에 배치 */
    public HttpHeaders setTokenHeaders(TokenDto tokenDto) {
        HttpHeaders headers = new HttpHeaders();
        ResponseCookie cookie = ResponseCookie.from("RefreshToken", tokenDto.getRefreshToken())
                .path("/")
                .maxAge(60*60*24*7) // 쿠키 유효기간 7일로 설정했음
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        headers.add("Set-cookie", cookie.toString());
        headers.add("Authorization", tokenDto.getAccessToken());

        return headers;
    }

    /* 회원가입 요청 처리 */
    public ResponseEntity<SignupResponseDto> kakaoSignup(@RequestBody SignupRequestDto requestDto) {
        // 받아온 정보 DB에 저장
        Account newAccount = Account.builder()
                .loginType("kakao")
                .authority(Authority.ROLE_USER)
                .email(requestDto.getAccount().getEmail())
                .kakaoName(requestDto.getAccount().getKakaoName())
                .nickname(requestDto.getNickname())
                .picture(requestDto.getPicture())
                .build();
        accountRepository.save(newAccount);

        // 회원가입 상황에 대해 토큰을 발급하고 헤더와 쿠키에 배치
        TokenDto tokenDto = securityService.signup(requestDto);
        saveRefreshToken(newAccount, tokenDto);
        HttpHeaders headers = setTokenHeaders(tokenDto);

        // 응답 작성
        SignupResponseDto responseDto = new SignupResponseDto();
        responseDto.setAccount(accountRepository.findByEmail(requestDto.getAccount().getEmail())
                .orElseThrow(CEmailLoginFailedException::new));
        responseDto.setResult("회원가입이 완료되었습니다.");
        return ResponseEntity.ok().headers(headers).body(responseDto);
    }

    /* Refresh Token 을 Repository 에 저장하는 메소드 */
    public void saveRefreshToken(Account account, TokenDto tokenDto) {
        RefreshToken refreshToken = RefreshToken.builder()
                .key(account.getId())
                .token(tokenDto.getRefreshToken())
                .build();
        tokenRepository.save(refreshToken);
        System.out.println("토큰 저장이 완료되었습니다 : 계정 아이디 - " + account.getId() + ", refresh token - " + tokenDto.getRefreshToken());
    }

}