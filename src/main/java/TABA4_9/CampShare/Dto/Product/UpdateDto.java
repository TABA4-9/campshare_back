package TABA4_9.CampShare.Dto.Product;

import TABA4_9.CampShare.Entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateDto {
    private Product updateProduct;
    private Boolean updateSuccess;
}
