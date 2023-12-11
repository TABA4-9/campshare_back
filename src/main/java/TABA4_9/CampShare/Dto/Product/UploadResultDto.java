package TABA4_9.CampShare.Dto.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UploadResultDto {
    List<ProductDto> lendItem;
    public UploadResultDto(){
        lendItem = new ArrayList<>();
    }
}