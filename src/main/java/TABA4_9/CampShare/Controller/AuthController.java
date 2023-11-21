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

}