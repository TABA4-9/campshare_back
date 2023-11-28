package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public Product save(Product product){
        return productRepository.save(product);
    }
    public Product findById(Long id){
        return productRepository.findById(id);
    }

    public void delete(Product product){
        productRepository.delete(product);
    }

}
