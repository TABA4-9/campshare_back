package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private ProductService productService;
    @GetMapping("/product/data/main")
    public Product[] getThreeProduct(){
        Product[] product = new Product[3];
        product[0]=productService.find(1);
        product[1]=productService.find(2);
        product[2]=productService.find(3);
        return product;
    }

}//endClass
