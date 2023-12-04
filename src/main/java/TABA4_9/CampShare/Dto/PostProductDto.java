package TABA4_9.CampShare.Dto;

import TABA4_9.CampShare.Entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter @Setter
public class PostProductDto extends Product implements Serializable {

    private MultipartFile[] image;

}
