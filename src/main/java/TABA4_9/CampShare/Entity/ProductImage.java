package TABA4_9.CampShare.Entity;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Data
public class ProductImage {
    @Id
    private String uuid;
    private String imagePath;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product id;
}
