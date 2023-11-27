package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.Entity;
import TABA4_9.CampShare.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findById(int id);

}
