package TABA4_9.CampShare.Service;


import TABA4_9.CampShare.Entity.ViewLog;
import TABA4_9.CampShare.Repository.ViewLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewLogService {
    private final ViewLogRepository viewLogRepository;
    public void save(ViewLog viewLog){
        viewLogRepository.save(viewLog);
    }

    public Optional<ViewLog> findByUserId(Long id){
        return viewLogRepository.findByUserId(id);
    }
    public Optional<List<ViewLog>> findById(Long id){
        return viewLogRepository.findAllByItemId(id);
    }
    public Optional<List<ViewLog>> findAll(){
        return Optional.of(viewLogRepository.findAll());
    }

    public Optional<List<ViewLog>> deleteAllByItemId(Long id) {
        return viewLogRepository.deleteAllByItemId(id);
    }

}
