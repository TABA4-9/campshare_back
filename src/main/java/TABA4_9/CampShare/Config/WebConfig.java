package TABA4_9.CampShare.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /* 프론트 서버와의 CORS 이슈를 해결하기 위한 메소드입니다.*/
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")

                //직접 만든 헤더 이름을 받을 수 있도록 명시해줍시다
                .exposedHeaders("Authorization", "Set-Cookie")
                .allowedMethods("*")
                .allowCredentials(true);
    }
}
