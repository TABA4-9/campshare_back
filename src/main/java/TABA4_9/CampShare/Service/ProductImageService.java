package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Entity.ProductImage;
import TABA4_9.CampShare.Repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {
    @Autowired
    ProductImageRepository productImageRepository;

    public String save(ProductImage productImage){
        productImageRepository.save(productImage);
        return productImage.getUuid();
    }

    public ProductImage findById(Long id) {
        return productImageRepository.findById(id);
    }

    public ProductImage findByUuid(String uuid) {
        return productImageRepository.findByUuid(uuid);
    }

    public void delete(ProductImage productImage){
        productImageRepository.delete(productImage);
    }
}
