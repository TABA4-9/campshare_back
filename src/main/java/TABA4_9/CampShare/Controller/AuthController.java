package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Entity.*;
import TABA4_9.CampShare.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/*

OAuth Kakao 인증 관련 요청을 처리하는 API 입니다.

*/
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /*
    인가코드로 카카오 서버에 액세스 토큰을 요청하고,
    해당 토큰으로 사용자 정보를 받아와 DB에 저장하는 API 입니다.
    GET 방식으로 param 에 들어오는 인가코드를 추출하여 처리 로직을 수행합니다.

    🤔 생각해볼 것 :
        - 유저 정보를 불러오는 과정은 다른 컨트롤러 단으로 분리
        - 로그인 요청이 들어오면, JWT 를 매번 발급
        - refresh token 만료시
    */

    @ResponseBody
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(HttpServletRequest request) {

        String code = request.getParameter("code");
        String kakaoAccessToken = authService.getKakaoAccessToken(code).getAccess_token();
        return authService.kakaoLogin(kakaoAccessToken);
    }

    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> kakaoSignup(@RequestBody SignupRequestDto requestDto) {
        return authService.kakaoSignup(requestDto);
    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<SignupResponseDto> kakaoSignup(@RequestBody SignupRequestDto requestDto) {
//
//        // requestDto 로 데이터 받아와서 accountId 반환
//        Long accountId = authService.kakaoSignUp(requestDto);
//        Account account = authService.accountFindById(accountId);
//
//        // 최초 가입자에게는 RefreshToken, AccessToken 모두 발급
//        TokenDto tokenDto = securityService.signup(accountId);
//
//        // AccessToken 은 header 에 세팅하고, refreshToken 은 httpOnly 쿠키로 세팅
//        HttpHeaders headers = authService.setTokenHeaders(tokenDto);
//
//        // 응답 작성
//        SignupResponseDto signUpResponseDto = new SignupResponseDto();
//        signUpResponseDto.setEmail(account.getEmail());
//        signUpResponseDto.setAccountName(account.getAccountName());
//        signUpResponseDto.setPicture(account.getPicture());
//
//        return ResponseEntity.ok().headers(headers).body(signUpResponseDto);
//    }
//
//    @GetMapping("/reissue")
//    public ResponseEntity reissue(HttpServletRequest request,
//                                  @CookieValue(name = "RefreshToken") Cookie cookie) {
//        String accessToken = request.getHeader("Authorization");
//        System.out.println("뽑아낸 access token: " + accessToken); //확인용
//        String refreshToken = cookie.getValue();
//        System.out.println("뽑아낸 refresh token: " + refreshToken); //확인용
//
//        TokenRequestDto tokenRequestDto = new TokenRequestDto(accessToken, refreshToken);
//        TokenDto newTokenDto = securityService.reissue(tokenRequestDto);
//
//        HttpHeaders headers = authService.setTokenHeaders(newTokenDto);
//
//        return ResponseEntity.ok().headers(headers).body("토큰 재발행이 완료되었습니다");
//    }
//
//    @GetMapping("/refresh")
//    public ResponseEntity refresh(@CookieValue(name = "RefreshToken") Cookie cookie) {
//
//        String refreshToken = cookie.getValue(); //쿠키속 refreshToken 가져오기
//        RefreshResponseDto responseDto = securityService.refresh(refreshToken);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", responseDto.getAccessToken());
//        return ResponseEntity.ok().headers(headers).body(responseDto.getAccount());
//    }
}