package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Dto.Product.DetailDto;
import TABA4_9.CampShare.Dto.Flask.FlaskProductDto;
import TABA4_9.CampShare.Dto.Flask.FlaskTestDto;
import TABA4_9.CampShare.Dto.Product.ProductDto;
import TABA4_9.CampShare.Dto.Product.RecommendItemDto;
import TABA4_9.CampShare.Entity.Account;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FlaskController {

    private final AccountService accountService;
    private final ProductService productService;
    private final ViewLogService viewLogService;
    private final FlaskService flaskService;

    @PostMapping("/detail/{itemId}")
    public List<ProductDto> flaskTest(@PathVariable("itemId") Long itemId, @RequestBody DetailDto detailDto) throws JsonProcessingException {
        List<ProductDto> recommandProduct = new ArrayList<>();
        Product product = productService.findById(itemId).orElseThrow();
        //log save start
        ViewLog viewLog = new ViewLog();
        viewLog.setItemId(product.getId());
        viewLog.setUserId(detailDto.getUserId());
        viewLog.setTimeStamp(detailDto.getDetailPageLog());
        viewLogService.save(viewLog);
        //log save end

        //flask search data
        FlaskTestDto flaskTestDto = new FlaskTestDto();
        flaskTestDto.setId(product.getId());
        flaskTestDto.setName(product.getName());
        //end

        //flask product
        List<FlaskProductDto> flaskProductDtoList = new ArrayList<>();

        List<Product> products = productService.findAll().get();

        for (int i = 0; i < products.size(); i++) {
            FlaskProductDto flaskProductDto = new FlaskProductDto();
            flaskProductDto.setId(products.get(i).getId());
            flaskProductDto.setName(products.get(i).getName());

            flaskProductDtoList.add(flaskProductDto);
        }
        //end

        //flask log
        List<ViewLog> flaskLogDtoList = viewLogService.findAll().get();
        //end

        //spring to flask
        List<Product> recommendProducts = sendToFlaskController(flaskProductDtoList,flaskLogDtoList,flaskTestDto);
        for (Product tempProduct : recommendProducts) {
            ProductDto productDto = new ProductDto(tempProduct);
            Optional<Account> account = accountService.findById(productDto.getPostUserId());
            productDto.setPostUserName(account.get().getName());
            productDto.setPostUserEmail(account.get().getEmail());
            recommandProduct.add(productDto);
        }

        return recommandProduct;
    }

    public List<Product> sendToFlaskController(List<FlaskProductDto> flaskProductDtoList, List<ViewLog> flaskLogDtoList, FlaskTestDto flaskTestDto) throws JsonProcessingException {
        RecommendItemDto recommendItemDto = flaskService.sendToFlask(flaskProductDtoList, flaskLogDtoList, flaskTestDto);

        Long item1 = recommendItemDto.getRecommendItemId1();
        Long item2 = recommendItemDto.getRecommendItemId2();
        Long item3 = recommendItemDto.getRecommendItemId3();

        List<Product> products = new ArrayList<>();

        products.add(productService.findById(item1).orElseThrow());
        products.add(productService.findById(item2).orElseThrow());
        products.add(productService.findById(item3).orElseThrow());

        return products;
    }
}
