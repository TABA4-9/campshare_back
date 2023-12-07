package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoAccountRepository extends JpaRepository<Account, String> {

    /* 이메일이 Login ID의 역할을 하기 때문에 이메일로 계정 찾는 메소드 구현 */
    Optional<Account> findByEmail(String email);
    /* 중복 가입 방지용 */
    boolean existsByEmail(String email);
}