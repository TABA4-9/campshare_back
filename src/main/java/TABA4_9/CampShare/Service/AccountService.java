package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Entity.Account;
import TABA4_9.CampShare.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Optional<Account> findById(Long id){
        return accountRepository.findById(id);
    }
}
