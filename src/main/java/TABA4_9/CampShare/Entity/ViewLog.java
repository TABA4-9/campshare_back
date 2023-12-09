package TABA4_9.CampShare.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class ViewLog {
    @Id
    private Long userId;
    private Long itemId;
    private String timeStamp;
}
