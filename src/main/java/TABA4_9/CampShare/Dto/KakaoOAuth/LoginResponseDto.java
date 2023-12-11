package TABA4_9.CampShare.Dto.KakaoOAuth;

import TABA4_9.CampShare.Dto.Product.ProductDto;
import TABA4_9.CampShare.Entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class LoginResponseDto {
    private boolean loginSuccess;
    private Account account;
    private String kakaoAccessToken;
    private List<ProductDto> lendItem;
    private List<ProductDto> rentItem;
}
