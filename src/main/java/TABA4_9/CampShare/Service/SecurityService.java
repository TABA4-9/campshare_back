package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.CEmailLoginFailedException;
import TABA4_9.CampShare.Dto.SignupRequestDto;
import TABA4_9.CampShare.Dto.TokenDto;
import TABA4_9.CampShare.Entity.Account;
import TABA4_9.CampShare.Entity.RefreshToken;
import TABA4_9.CampShare.JwtProvider;
import TABA4_9.CampShare.Repository.AccountRepository;
import TABA4_9.CampShare.Repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/*

토큰을 발급하는 모든 상황을 처리하는 Service

*/
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository tokenRepository;

    public static Long getCurrentAccountId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        return Long.parseLong(authentication.getName());
    }

    /* 로그인 된 사용자에게 토큰 발급 : refresh token 은 DB 에 저장 */
    public TokenDto login(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(CEmailLoginFailedException::new);

        System.out.println("SecurityService-login: 계정을 찾았습니다. " + account);
        // 토큰 발행
        TokenDto tokenDto = jwtProvider.generateTokenDto(email);
        // RefreshToken 만 DB에 저장
        // signup 시에도 저장하고, 로그인시에도 저장하므로 존재하는 토큰을 찾기 위해 key 값이 필요
        RefreshToken refreshToken = RefreshToken.builder()
                .key(account.getAccountId())
                .token(tokenDto.getRefreshToken())
                .build();
        tokenRepository.save(refreshToken);
        System.out.println("토큰 발급과 저장을 완료했습니다.");
        return tokenDto;
    }

    public TokenDto signup(SignupRequestDto requestDto) {
        Account account = requestDto.getAccount();
        return jwtProvider.generateTokenDto(account.getEmail());
    }
}