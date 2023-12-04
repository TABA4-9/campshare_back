package TABA4_9.CampShare.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

@Getter @Setter
public class KakaoAccountDto {

    private Long id;
    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;
    private Properties properties;
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    public static class KakaoAccount {
        @JsonProperty("profile_nickname_needs_agreement")
        private Boolean profileNicknameNeedsAgreement;
        @JsonProperty("email_needs_agreement")
        private Boolean emailNeedsAgreement;
        @JsonProperty("is_email_valid")
        private Boolean isEmailValid;
        @JsonProperty("is_email_verified")
        private Boolean isEmailVerified;
        @JsonProperty("has_email")
        private Boolean hasEmail;
        @Setter private String email;
        @Setter private KakaoProfile profile;

        @Getter @Setter
        public static class KakaoProfile {
            public String nickname;
        }
    }
}
