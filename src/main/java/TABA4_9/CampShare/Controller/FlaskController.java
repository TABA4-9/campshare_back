package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Dto.FlaskTestDto;
import TABA4_9.CampShare.Dto.RecommendItemDto;
import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Service.FlaskService;
import TABA4_9.CampShare.Service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FlaskController {

    private final ProductService productService;

    private final FlaskService flaskService;
    @PostMapping("/spring/test")
    public List<Product> sendToFlask(@RequestBody FlaskTestDto flaskTestDto) throws JsonProcessingException {

        Long item1, item2, item3;
        RecommendItemDto recommendItemDto = flaskService.sendToFlask(flaskTestDto);
        log.info("flask server received");
        log.info("{}", recommendItemDto);
        item1 = recommendItemDto.getRecommendItemId1();
        item2 = recommendItemDto.getRecommendItemId2();
        item3 = recommendItemDto.getRecommendItemId3();
        System.out.println("item1 = " + item1);
        System.out.println("item2 = " + item2);
        System.out.println("item3 = " + item3);

        //
        List<Product> products = new ArrayList<>();
        products.add(productService.findById(item1).get());
        products.add(productService.findById(item2).get());
        products.add(productService.findById(item3).get());


        System.out.println("products = " + products);


        return products;
    }

}
