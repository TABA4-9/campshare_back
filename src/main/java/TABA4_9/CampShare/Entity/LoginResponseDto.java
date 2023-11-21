package TABA4_9.CampShare.Entity;

import lombok.Data;

@Data
public class LoginResponseDto {
    public boolean loginSuccess;
    public Account account;
    public String kakaoAccessToken;
}
