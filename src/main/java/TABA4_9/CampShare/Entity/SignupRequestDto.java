package TABA4_9.CampShare.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    public String nickname;
    public String picture;
    public Account account;
}
