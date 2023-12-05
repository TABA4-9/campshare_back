package TABA4_9.CampShare.Service;


import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Entity.SearchLog;
import TABA4_9.CampShare.Repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchLogService {
    private final SearchLogRepository searchLogRepository;
    public SearchLog save(SearchLog searchLog){
        return searchLogRepository.save(searchLog);
    }
    public Optional<SearchLog> findById(Long id){
        return searchLogRepository.findByUserId(id);
    }


    public Optional<List<SearchLog>> findAll(){
        return Optional.of(searchLogRepository.findAll());
    }

//    public Optional<List<SearchLog>> findThree(){
//
//
//
//        return
//    }
    public void delete(SearchLog searchLog){
        searchLogRepository.deleteById(searchLog.getId());
    }
}
