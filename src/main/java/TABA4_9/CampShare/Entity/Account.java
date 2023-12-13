package TABA4_9.CampShare.Entity;

import lombok.*;

import javax.persistence.*;

/*

@Entity 어노테이션 붙이면 알아서 JPA 연동됨

 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "account")
@Entity
public class Account extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String loginType;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String name; //카카오닉네임

    private String nickname; //사용자별명

    @Column(nullable = false)
    private String email;

    /* 회원가입 과정에서는 프로필 사진을 나중에 등록할 수 있게 nullable */
    private String picture;

}