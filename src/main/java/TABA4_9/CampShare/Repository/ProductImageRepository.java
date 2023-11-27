package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    ProductImage findByUuid(String uuid);
    ProductImage findById(Long id);
}
