package TABA4_9.CampShare.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Properties;

@Getter @Setter
public class KakaoAccountDto {

    private Long id;
    private LocalDateTime connectedAt;
    private Properties properties;
    private KakaoAccount kakaoAccount;

    @Getter
    public static class KakaoAccount {
        private Boolean profileNicknameNeedsAgreement;
        private Boolean emailNeedsAgreement;
        private Boolean isEmailValid;
        private Boolean isEmailVerified;
        private Boolean hasEmail;
        @Setter private String email;
        @Setter private KakaoProfile profile;

        @Getter @Setter
        public static class KakaoProfile {
            public String nickname;
        }
    }
}
