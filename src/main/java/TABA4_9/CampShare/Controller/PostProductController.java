package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Entity.PostProduct;
import TABA4_9.CampShare.Service.PostProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * name String 상품 이름
 * period String 기간 설정
 * category String 상품 분류
 * headcount Number 인원 수
 * usingYear String 사용 연수
 * brand String  상품 브랜드
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PostProductController {

    private static Long sequence = 0L;
    public final PostProductService postProductService;


    /**
     *                     <p>
     *                     period String 기간 설정
     *                     <p>
     *                     category String 상품 분류
     *                     <p>
     *                     headcount Number 인원 수
     *                     <p>
     *                     usingYear String 사용 연수
     *                     <p>
     *                     brand String 상품 브랜드
     *                     <p>
     *                     Responses200: OK
     */

    @ExceptionHandler
    @PostMapping("/post/nextPage")
    protected String postProduct1(@RequestBody PostProduct product, Exception e){
        return PostProductService.minPrice(product);
    }


    /**
     * price*Number상품 가격
     * <p>
     * explanationString상품 설명
     * <p>
     * image*String상품 이미지
     * <p>
     * address*String대여 희망 주소
     * <p>
     * userID*Number유저 ID
     * <p>
     * Responses200: OK
     * <p>
     * id가 포함된 object로 전달
     */


    @PostMapping("/post/submit")
    public PostProduct postProduct2(@RequestBody PostProduct product) throws IOException {
        PostProduct postProductFinal = product;
        postProductFinal.setTimestamp(setTimeStamp());
        postProductService.save(postProductFinal);
        return postProductFinal;
    }

    public String setTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
