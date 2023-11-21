package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Entity, Integer> {
    Entity findByContent(String content);
}
