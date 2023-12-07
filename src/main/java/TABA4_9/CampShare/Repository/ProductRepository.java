package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findByPostUserId(Long UserId);
    Optional<List<Product>> findByRentUserId(Long UserId);
    Optional<List<Product>> findByNameLike(String name);

}
