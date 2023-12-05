package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public Product save(Product product){
        return productRepository.save(product);
    }
    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }
    public Optional<List<Product>> findByPostUserId(Long postUserId){
        return productRepository.findByPostUserId(postUserId);
    }

    public Optional<List<Product>> findByRentUserId(Long rentUserId){
        return productRepository.findByRentUserId(rentUserId);
    }

    public Optional<List<Product>> findAll(){
        return Optional.of(productRepository.findAll());
    }
    public void delete(Product product){
        productRepository.deleteById(product.getId());
    }

}
