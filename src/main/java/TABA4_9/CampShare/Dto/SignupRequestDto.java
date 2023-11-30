package TABA4_9.CampShare.Dto;

import TABA4_9.CampShare.Entity.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String nickname;
    private String picture;
    private Account account;
}
