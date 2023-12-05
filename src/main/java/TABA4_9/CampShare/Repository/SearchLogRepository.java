package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
    Optional<SearchLog> findByUserId(Long UserId);

}
