package TABA4_9.CampShare.Dto;


import lombok.Getter;

@Getter
public class KakaoTokenDto {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private String id_token;
    private int expires_in;
    private int refresh_token_expires_in;
    private String scope;

    public KakaoTokenDto(){
        access_token = null;
    }

    public void setAccess_token(String accessToken) {
        this.access_token = accessToken;
    }
}