package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Dto.Product.DetailDto;
import TABA4_9.CampShare.Dto.Flask.FlaskProductDto;
import TABA4_9.CampShare.Dto.Flask.FlaskTestDto;
import TABA4_9.CampShare.Dto.Product.RecommendItemDto;
import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Entity.ViewLog;
import TABA4_9.CampShare.Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FlaskController {
    private final ProductService productService;
    private final ViewLogService viewLogService;
    private final FlaskService flaskService;

    @PostMapping("flask/test/{itemId}")
    public List<Product> flaskTest(@PathVariable("itemId") Long itemId, @RequestBody DetailDto detailDto) throws JsonProcessingException {
        List<Product> showProducts = new ArrayList<>();
        Product product = productService.findById(itemId).orElseThrow();
        log.info("flask/test -> product : {}", product);
        //log save start
        ViewLog viewLog = new ViewLog();
        viewLog.setItemId(product.getId());
        viewLog.setUserId(detailDto.getUserId());
        viewLog.setTimeStamp(detailDto.getDetailPageLog());
        viewLogService.save(viewLog);

        log.info("log save");
        //log save end

        //flask search data
        FlaskTestDto flaskTestDto = new FlaskTestDto();
        flaskTestDto.setId(product.getId());
        flaskTestDto.setName(product.getName());

        log.info("flaskTestDto set end");
        //end

        //flask product
        List<FlaskProductDto> flaskProductDtoList = new ArrayList<>();

        List<Product> products = productService.findAll().get();

        for (int i = 0; i < products.size(); i++) {
            FlaskProductDto flaskProductDto = new FlaskProductDto();
            flaskProductDto.setId(products.get(i).getId());
            flaskProductDto.setName(products.get(i).getName());

            flaskProductDtoList.add(flaskProductDto);
            log.info("flaskProductDtoList = {} / {}", flaskProductDto.getId(), flaskProductDto.getName());

        }
        //end
        log.info("end flask product");

        //flask log
        log.info("start flask log");
        List<ViewLog> flaskLogDtoList = viewLogService.findAll().get();
        //end

        //spring to flask
        log.info("start sendToFlask");
        List<Product> recommendProducts = sendToFlaskController(flaskProductDtoList,flaskLogDtoList,flaskTestDto);

        log.info("end sendToFlask");
        showProducts.add(product);
        showProducts.add(recommendProducts.get(0));
        showProducts.add(recommendProducts.get(1));
        showProducts.add(recommendProducts.get(2));

        return showProducts;
    }

        public List<Product> sendToFlaskController(List<FlaskProductDto> flaskProductDtoList, List<ViewLog> flaskLogDtoList, FlaskTestDto flaskTestDto) throws JsonProcessingException {
        log.info("into sendToFlask");
        RecommendItemDto recommendItemDto = flaskService.sendToFlask(flaskProductDtoList, flaskLogDtoList, flaskTestDto);

        Long item1 = recommendItemDto.getRecommendItemId1();
        Long item2 = recommendItemDto.getRecommendItemId2();
        Long item3 = recommendItemDto.getRecommendItemId3();

        log.info("{} , {}, {}", item1, item2, item3);

        List<Product> products = new ArrayList<>();

        products.add(productService.findById(item1).orElseThrow());
        products.add(productService.findById(item2).orElseThrow());
        products.add(productService.findById(item3).orElseThrow());

        return products;
    }

}
