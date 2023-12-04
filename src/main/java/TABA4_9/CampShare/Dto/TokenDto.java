package TABA4_9.CampShare.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenDto {

    private String grant_type;
    private String access_token;
    private String refresh_token;
    private Long access_token_expires_in;
}