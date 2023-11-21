package TABA4_9.CampShare.Entity;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;

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
public class Account extends BaseTimeEntity { // 예약어가 이미 존재하므로 users로 바꾸어 지정해야함

    @Id
    @Column(name="account_id")
    private Long id;

    @Column
    private String loginType;

    @Column
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column
    private String kakaoName; //카카오닉네임

    @Column
    private String nickname; //사용자별명

    @Column(nullable = false)
    private String email;

    /* 회원가입 과정에서는 프로필 사진을 나중에 등록할 수 있게 nullable */
    @Column
    private String picture;

}