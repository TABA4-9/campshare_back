package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Dto.FlaskTestDto;
import TABA4_9.CampShare.Dto.RecommendItemDto;
import TABA4_9.CampShare.Entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlaskService {

    //데이터를 JSON 객체로 변환하기 위해서 사용
    private final ObjectMapper objectMapper;

    @Transactional
    public RecommendItemDto sendToFlask(FlaskTestDto flaskTestDto) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        //헤더를 JSON으로 설정함
        HttpHeaders headers = new HttpHeaders();

        //파라미터로 들어온 dto를 JSON 객체로 변환
        headers.setContentType(MediaType.APPLICATION_JSON);

        String param = objectMapper.writeValueAsString(flaskTestDto);

        HttpEntity<String> entity = new HttpEntity<>(param , headers);

        //실제 Flask 서버랑 연결하기 위한 URL
        String url = "http://localhost:5000/test";


        String recommend_json = restTemplate.postForObject(url, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        RecommendItemDto recommendItemDto = objectMapper.readValue(recommend_json, RecommendItemDto.class);

        return recommendItemDto;


        //Flask 서버로 데이터를 전송하고 받은 응답 값을 return

    }



}