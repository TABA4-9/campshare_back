package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Entity.SearchLog;
import TABA4_9.CampShare.Service.SearchLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SearchLogController {
    private final SearchLogService searchLogService;



    public void saveLog(Optional<Product> product, Long userId, String timeStamp) {
        SearchLog searchLog = new SearchLog();
        searchLog.setItemId(product.orElseThrow().getId());
        searchLog.setUserId(userId);
        searchLog.setTimeStamp(timeStamp);

        searchLogService.save(searchLog);
    }

}
