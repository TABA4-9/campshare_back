package TABA4_9.CampShare.Config;

import TABA4_9.CampShare.CustomAccessDeniedHandler;
import TABA4_9.CampShare.CustomAuthenticationEntryPoint;
import TABA4_9.CampShare.JwtAuthenticationFilter;
import TABA4_9.CampShare.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers( "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf().disable()
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .cors() // CORS 에러 방지용

                // 시큐리티는 기본적으로 세션을 사용
                // 세션을 사용하지 않을거라 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 접근 권한 설정부
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll() // 열어두어야 CORS Preflight 막을 수 있음
                .antMatchers("/", "/**").permitAll()
                .anyRequest().permitAll()

                // JWT 토큰 예외처리부
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
