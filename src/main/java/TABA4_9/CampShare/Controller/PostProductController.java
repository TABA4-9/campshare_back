package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Entity.PostProduct;
import TABA4_9.CampShare.Repository.PostProductRepository;
import TABA4_9.CampShare.Service.PostProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


/**
 * name String 상품 이름
 * period String 기간 설정
 * category String 상품 분류
 * headcount Number 인원 수
 * usingYear String 사용 연수
 * brand String  상품 브랜드
 */
@Slf4j
@Controller
public class PostProductController {
    private ObjectMapper objectMapper = new ObjectMapper();

    private PostProductRepositoryController itemRepository = PostProductRepositoryController.getInstance();
    public PostProduct postProductFinal;


    public PostProductRepository postProductRepository;
    public PostProductController() {
    }

    /**
     * @param product
     * @throws IOException name String 상품 이름
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
    @ResponseBody
    @PostMapping("/post/nextPage")
    public PostProduct postProduct1(@RequestBody PostProduct product) throws IOException {
        log.info("/post/nextPage start, product={}", product);
        postProductFinal = new PostProduct();
        postProductFinal.setName(product.getName());
        postProductFinal.setPeriod(product.getPeriod());
        postProductFinal.setCategory(product.getCategory());
        postProductFinal.setHeadcount(product.getHeadcount());
        postProductFinal.setUsingYear(product.getUsingYear());
        postProductFinal.setBrand(product.getBrand());
        postProductFinal.setBrand(product.getBrand());
        itemRepository.saveID(postProductFinal);
        log.info("/post/nextPage end, product={}", postProductFinal);
        return postProductFinal;
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

    @ResponseBody
    @PostMapping("/post/submit")
    public PostProduct postProduct2(@RequestBody PostProduct product) throws IOException {
        log.info("/post/submit start, product={}", product);
        postProductFinal.setPrice(product.getPrice());
        postProductFinal.setExplanation(product.getExplanation());
        postProductFinal.setImage(product.getImage());
        postProductFinal.setAddress(product.getAddress());
        postProductFinal.setUserID(product.getUserID());
        PostProduct returnData = itemRepository.save(postProductFinal);

        //db save
//        PostProductService postProductService = new PostProductService(postProductRepository);
//        postProductService.save(returnData);
        //
        log.info("/post/submit end, product={}", postProductFinal);
        log.info("/post/submit final end, itemRepository={}", itemRepository);
        return returnData;
    }

}
