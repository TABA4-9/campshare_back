package TABA4_9.CampShare.Entity;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String name;
    private String category;
    private Long headcount;
    private String explanation;
    private Long price;
    private String image;
    private String brand;
    private String usingYear;
    private String period;
}
