package TABA4_9.CampShare.Dto.Product;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeleteDto {
    private Long productId;
    private Boolean deleteSuccess;
}