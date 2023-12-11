package TABA4_9.CampShare.Dto.Product;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class DeleteDto {
    private Long productId;
    private Boolean deleteSuccess;
    List<ProductDto> lendItem;
    public DeleteDto(){
        lendItem = new ArrayList<>();
    }
}
