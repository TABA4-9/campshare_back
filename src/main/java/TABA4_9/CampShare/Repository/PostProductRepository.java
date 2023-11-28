package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.PostProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostProductRepository extends JpaRepository<PostProduct, Long> {
    Optional<PostProduct> findById(Long id);
}
