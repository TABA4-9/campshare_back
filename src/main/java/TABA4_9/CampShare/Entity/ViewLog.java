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
public class ViewLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long viewId;
    private Long userId;
    private Long itemId;
    private String timeStamp;
}
