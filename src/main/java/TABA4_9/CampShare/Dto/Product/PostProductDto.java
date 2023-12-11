package TABA4_9.CampShare.Dto.Product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Getter @Setter
public class PostProductDto implements Serializable {
    private Long id;
    private String name;
    private String startDate;
    private String endDate;
    private String category;
    private String headcount;
    private String explanation;
    private String price;
    private List<String> imagePath;
    private String imageUrl;
    private String brand;
    private String usingYear;
    private String address;
    private String postUserId;
    private String isRented;
    private String rentUserId;
    private String timestamp;
    private MultipartFile[] image;

}
