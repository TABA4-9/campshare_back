package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Entity.*;
import TABA4_9.CampShare.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/*

OAuth Kakao 인증 관련 요청을 처리하는 API 입니다.

*/
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@Valid HttpServletRequest request) {

        String code = request.getParameter("code");
        String kakaoAccessToken = authService.getKakaoAccessToken(code).getAccessToken();
        return authService.kakaoLogin(kakaoAccessToken);
    }

}//endClass