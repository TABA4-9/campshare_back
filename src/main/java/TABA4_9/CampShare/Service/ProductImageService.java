package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Entity.ProductImage;
import TABA4_9.CampShare.Repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public String save(ProductImage productImage){
        productImageRepository.save(productImage);
        return productImage.getUuid();
    }

    public Optional<ProductImage> findById(Long id) {
        return productImageRepository.findById(String.valueOf(id));
    }

    public ProductImage findByUuid(String uuid) {
        return productImageRepository.findByUuid(uuid);
    }

    public void delete(ProductImage productImage){
        productImageRepository.delete(productImage);
    }
}
