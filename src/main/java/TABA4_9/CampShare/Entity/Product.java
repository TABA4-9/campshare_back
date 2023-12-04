package TABA4_9.CampShare.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Getter
@Setter
public class Product {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String name;
    private String category;
    private String headcount;
    private String explanation;
    private String price;
    private String imagePath;
    private String brand;
    private String usingYear;
    private String period;
    private String address;
    private Long postUserId;
    private Boolean isRented;
    private Long rentUserId;
    private String timestamp;

    public Product() {

    }

    public Product(Product product){
        id = product.getId();
        name = product.getName();
        category = product.getCategory();
        headcount = product.getHeadcount();
        explanation = product.getExplanation();
        price = product.getPrice();
        imagePath = product.getImagePath();
        brand = product.getBrand();
        usingYear = product.getUsingYear();
        period = product.getPeriod();
        address = product.getAddress();
        postUserId = product.getPostUserId();
        isRented = product.getIsRented();
        rentUserId = product.getRentUserId();
        timestamp = product.getTimestamp();
    }

}
