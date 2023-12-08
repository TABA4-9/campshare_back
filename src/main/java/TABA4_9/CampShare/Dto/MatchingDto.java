package TABA4_9.CampShare.Dto;

import TABA4_9.CampShare.Entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class MatchingDto {
    Long productId;
    Long rentUserId;
    @Setter
    List<Product> rentItem;
}
