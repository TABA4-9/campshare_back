package TABA4_9.CampShare.Entity;

import TABA4_9.CampShare.Dto.PostProductDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Getter
@Setter
public class Product{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String name;
    private String startDate;
    private String endDate;
    private String category;
    private String headcount;
    private String explanation;
    private String price;
    private String imagePath;
    private String brand;
    private String usingYear;
    private String address;
    private Long postUserId;
    private Boolean isRented;
    private Long rentUserId;
    private String timestamp;

    public Product() {

    }

    public Product(PostProductDto postProductDto){
        name = postProductDto.getName();
        brand = postProductDto.getBrand();
        category = postProductDto.getCategory();
        usingYear = postProductDto.getUsingYear();
        headcount = postProductDto.getHeadcount();
        explanation = postProductDto.getExplanation();
        price = postProductDto.getPrice();
        address = postProductDto.getAddress();
        startDate = postProductDto.getStartDate();
        endDate = postProductDto.getEndDate();
        postUserId = Long.valueOf(postProductDto.getPostUserId());
        isRented = Boolean.valueOf(postProductDto.getIsRented());
    }

}
