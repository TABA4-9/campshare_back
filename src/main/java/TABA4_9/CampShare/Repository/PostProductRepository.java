package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.PostProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostProductRepository extends JpaRepository<PostProduct, Integer> {
    PostProduct findById(Long id);
}
