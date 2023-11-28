package TABA4_9.CampShare.Entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "uuid")
public class ProductImage {
    @Id
    private String uuid;
    private String imagePath;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
