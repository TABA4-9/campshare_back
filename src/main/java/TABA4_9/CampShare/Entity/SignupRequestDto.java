package TABA4_9.CampShare.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String nickname;
    private String picture;
    private Account account;
}
