package TABA4_9.CampShare.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
name String 상품 이름

period String 기간 설정

category String 상품 분류

headcount Number 인원 수

usingYear String 사용 연수

brand String 상품 브랜드

Responses200: OK

price*Number상품 가격

explanationString상품 설명

image*String상품 이미지

 아래부터 추가

address*String대여 희망 주소

userID*Number유저 ID

Responses200: OK

id가 포함된 object로 전달
 */

@Entity
@Data
public class PostProduct {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String name; //ok
    private String category; //ok
    private Long headcount; //ok
    private String explanation; //ok
    private Long price; //ok
    private String image; //ok
    private String brand; //ok
    private String usingYear; //ok
    private String period; //ok

    private String address;

    private Long userID;

}
