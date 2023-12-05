package TABA4_9.CampShare.Dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoTokenDto {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token_expires_in")
    private int refreshTokenExpiresIn;

    private String scope;

    public KakaoTokenDto(){
        accessToken = null;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}