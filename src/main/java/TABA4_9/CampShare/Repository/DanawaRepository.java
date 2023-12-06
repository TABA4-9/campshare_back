package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Dto.DanawaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DanawaRepository extends JpaRepository<DanawaDto, String> {
    Optional<List<DanawaDto>> findByPeople(Long people);

}
