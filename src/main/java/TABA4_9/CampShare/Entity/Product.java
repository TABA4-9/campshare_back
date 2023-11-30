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
    private Long headcount;
    private String explanation;
    private Long price;
    private String image;
    private String brand;
    private String usingYear;
    private String period;
    private String address;
    private Long userID;
    private Boolean isRented;
    private String  timestamp;
}
