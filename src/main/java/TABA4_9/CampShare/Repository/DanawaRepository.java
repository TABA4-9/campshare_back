package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.Danawa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DanawaRepository extends JpaRepository<Danawa, String> {
    Optional<List<Danawa>> findByPeople(Long people);

}
