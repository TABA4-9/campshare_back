package TABA4_9.CampShare.Dto.Product;

import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Service.AccountService;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ProductDto {
    private Long id;
    private String name;
    private String startDate;
    private String endDate;
    private String category;
    private String headcount;
    private String explanation;
    private String price;
    private List<String> imagePath;
    private String brand;
    private String usingYear;
    private String address;
    private Long postUserId;
    private Boolean isRented;
    private Long rentUserId;
    private String timestamp;

    private String postUserName;

    private String postUserEmail;



    public ProductDto(){}
    public ProductDto(Product product){
        id = product.getId();
        name = product.getName();
        startDate = product.getStartDate();
        endDate = product.getEndDate();
        category = product.getCategory();
        headcount = product.getHeadcount();
        explanation = product.getExplanation();
        price = product.getPrice();
        imagePath = new ArrayList<>(3);
        imagePath.add(product.getImagePath1());
        imagePath.add(product.getImagePath2());
        imagePath.add(product.getImagePath3());
        brand = product.getBrand();
        usingYear = product.getUsingYear();
        address = product.getAddress();
        postUserId = product.getPostUserId();
        isRented = product.getIsRented();
        rentUserId = product.getRentUserId();
        timestamp = product.getTimestamp();
    }
}
