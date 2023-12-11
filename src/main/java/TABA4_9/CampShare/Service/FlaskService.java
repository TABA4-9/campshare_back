package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Dto.Flask.FlaskProductDto;
import TABA4_9.CampShare.Dto.Flask.FlaskTestDto;
import TABA4_9.CampShare.Dto.Product.RecommendItemDto;
import TABA4_9.CampShare.Entity.ViewLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlaskService {

    //데이터를 JSON 객체로 변환하기 위해서 사용
    private final ObjectMapper objectMapper;

    @Transactional
    public RecommendItemDto sendToFlask(List<FlaskProductDto> flaskProductDtoList, List<ViewLog> flaskLogDtoList, FlaskTestDto flaskTestDto) throws JsonProcessingException {
        log.info("1");
        RestTemplate restTemplate = new RestTemplate();

        log.info("2");
        //헤더를 JSON으로 설정함
        HttpHeaders headers = new HttpHeaders();

        log.info("3");
        //파라미터로 들어온 dto를 JSON 객체로 변환
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.info("4");
        String productParam = objectMapper.writeValueAsString(flaskProductDtoList);
        String logParam = objectMapper.writeValueAsString(flaskLogDtoList);
        String searchParam = objectMapper.writeValueAsString(flaskTestDto);

        log.info("productparam : {}", productParam);
        log.info("logparam : {}", logParam);
        log.info("searchparam : {}", searchParam);

        log.info("5");
        HttpEntity<String> productEntity = new HttpEntity<>(productParam , headers);
        HttpEntity<String> logEntity = new HttpEntity<>(logParam , headers);
        HttpEntity<String> searchEntity = new HttpEntity<>(searchParam , headers);

        log.info("6");
        //실제 Flask 서버랑 연결하기 위한 URL
        String productUrl = "http://43.200.201.220:5000/test/product";
        String logUrl = "http://43.200.201.220:5000/test/log";
        String searchUrl = "http://43.200.201.220:5000/test/search";

        String product_json = restTemplate.postForObject(productUrl, productEntity, String.class);
        System.out.println("product_json = " + product_json);
        String log_json = restTemplate.postForObject(logUrl, logEntity, String.class);
        System.out.println("log_json = " + log_json);
        String recommend_json = restTemplate.postForObject(searchUrl, searchEntity, String.class);
        System.out.println("recommend_json = " + recommend_json);

        ObjectMapper objectMapper = new ObjectMapper();
        //Flask 서버로 데이터를 전송하고 받은 응답 값을 return
        RecommendItemDto recommendItemDto = objectMapper.readValue(recommend_json, RecommendItemDto.class);
        return recommendItemDto;
    }



}