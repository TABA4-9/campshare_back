package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Entity.Danawa;
import TABA4_9.CampShare.Repository.DanawaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DanawaService {

    private final DanawaRepository danawaRepository;

    public Optional<List<Danawa>> findByPeople(Long people){
        return danawaRepository.findByPeople(people);
    }

}
