package TABA4_9.CampShare.Entity;

import lombok.Data;

@Data
public class SignupRequestDto {

    public String nickname;
    public String picture;
    public Account account;
}
