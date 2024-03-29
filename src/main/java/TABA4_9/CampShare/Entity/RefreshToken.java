package TABA4_9.CampShare.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {
    @Id
    @Column(name = "token_key", nullable = false)
    private Long key;

    @Column(name = "token_value", nullable = false)
    private String token;

    @Builder
    public RefreshToken(Long key, String token) {
        this.key = key;
        this.token = token;
    }

    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }
}