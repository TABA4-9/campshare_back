package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Dto.FlaskTestDto;
import TABA4_9.CampShare.Service.FlaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FlaskController {
    private final FlaskService flaskService;
    @PostMapping("/spring/test")
    public String sendToFlask(@RequestBody FlaskTestDto flaskTestDto) throws JsonProcessingException {
        return flaskService.sendToFlask(flaskTestDto);
    }

}
