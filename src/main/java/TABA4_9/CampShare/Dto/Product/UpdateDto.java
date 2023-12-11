package TABA4_9.CampShare.Dto.Product;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class UpdateDto {
    List<ProductDto> lendItem;
    private Boolean updateSuccess;

    public UpdateDto(){
        lendItem = new ArrayList<>();
    }
}
