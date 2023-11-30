package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
    List<ProductImage> findByProductId(Long productId);

    ProductImage findByUuid(String uuid);
}
