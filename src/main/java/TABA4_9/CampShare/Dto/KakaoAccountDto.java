package TABA4_9.CampShare.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Properties;

@Getter @Setter
public class KakaoAccountDto {

    private Long id;
    private Date connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter
    public static class KakaoAccount {
        private Boolean profile_nickname_needs_agreement;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private Boolean has_email;
        @Setter private String email;
        @Setter private KakaoProfile profile;

        @Getter @Setter
        public static class KakaoProfile {
            public String nickname;
        }
    }
}
