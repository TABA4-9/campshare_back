package TABA4_9.CampShare.Dto;

import TABA4_9.CampShare.Entity.Account;
import TABA4_9.CampShare.Entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter @Setter
public class LoginResponseDto {
    private boolean loginSuccess;
    private Account account;
    private String kakaoAccessToken;
    private Optional<List<Product>> postedProducts;
    private Optional<List<Product>> rentedProducts;
}
