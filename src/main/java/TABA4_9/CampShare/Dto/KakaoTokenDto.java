package TABA4_9.CampShare.Dto;


import lombok.Getter;

@Getter
public class KakaoTokenDto {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String idToken;
    private int expiresIn;
    private int refreshTokenExpiresIn;
    private String scope;

    public KakaoTokenDto(){
        accessToken = null;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}