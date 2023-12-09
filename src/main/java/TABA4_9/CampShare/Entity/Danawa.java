package TABA4_9.CampShare.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity @Table @Getter
public class Danawa {
    @Id
    private String name;

    private Long price;

    private Long people;

}
