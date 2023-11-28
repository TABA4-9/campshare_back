package TABA4_9.CampShare.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private Long usingYear;
    private String period;
}
