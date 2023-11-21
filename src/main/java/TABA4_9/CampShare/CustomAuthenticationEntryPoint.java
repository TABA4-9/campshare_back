package TABA4_9.CampShare;

/*

AuthenticationEntryPoint
정상적인 JWT 가 오지 않은 경우를 필터링합니다.

✅ 예외가 발생할 경우 /exception/entryPoint 로 리다이렉트하여 처리하도록 했습니다.
(ExceptionController -> CAuthenticationEntryPointException 호출)

*/

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED); //401 에러 발생시키기
    }

}
