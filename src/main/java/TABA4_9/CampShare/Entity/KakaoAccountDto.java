package TABA4_9.CampShare.Entity;

import lombok.Data;

import java.util.Properties;

@Data
public class KakaoAccountDto {

    public Long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Data
    public static class KakaoAccount {
        public Boolean profile_nickname_needs_agreement;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public Boolean has_email;

        public String email;
        public KakaoProfile profile;

        @Data
        public static class KakaoProfile {
            public String nickname;
        }
    }
}
