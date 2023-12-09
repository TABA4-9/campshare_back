package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.ViewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewLogRepository extends JpaRepository<ViewLog, Long> {
    Optional<ViewLog> findByUserId(Long UserId);

}
