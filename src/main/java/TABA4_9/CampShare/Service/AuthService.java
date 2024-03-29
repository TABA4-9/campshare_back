package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.CEmailLoginFailedException;
import TABA4_9.CampShare.Dto.KakaoOAuth.KakaoAccountDto;
import TABA4_9.CampShare.Dto.KakaoOAuth.KakaoTokenDto;
import TABA4_9.CampShare.Dto.KakaoOAuth.LoginResponseDto;
import TABA4_9.CampShare.Dto.KakaoOAuth.TokenDto;
import TABA4_9.CampShare.Dto.Product.ProductDto;
import TABA4_9.CampShare.Entity.Account;
import TABA4_9.CampShare.Entity.Authority;
import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Repository.KakaoAccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final ProductService productService;
    private final KakaoAccountRepository kakaoAccountRepository;
    private final SecurityService securityService;
    private final AccountService accountService;
    /* 환경변수 가져오기 */
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String KAKAO_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    String CLIENT_SECRET;

    /* 인가코드로 kakaoAccessToken 따오는 메소드 */
    public KakaoTokenDto getKakaoAccessToken(String code) {

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
        params.add("client_id", KAKAO_CLIENT_ID); //카카오 앱 REST API 키
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("code", code); //인가 코드 요청시 받은 인가 코드값, 프론트에서 받아오는 그 코드
        params.add("client_secret", CLIENT_SECRET);

        // 헤더와 바디 합치기 위해 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        log.debug("[카카오 서버로 보낼 POST] {}", kakaoTokenRequest);

        // 카카오로부터 Access token 수신
        try{
            ResponseEntity<String> kakaoTokenResponse = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            log.debug("카카오 서버로 code를 POST 전송 완료");
            log.debug("[카카오 서버로부터 받아온 토큰] {}", kakaoTokenResponse);

            // JSON Parsing (-> KakaoTokenDto)
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            KakaoTokenDto kakaoTokenDto = null;

            try {
                kakaoTokenDto = objectMapper.readValue(kakaoTokenResponse.getBody(), KakaoTokenDto.class);
            }

            catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            log.debug("[object mapping 이후] {}", kakaoTokenDto);

            return kakaoTokenDto;
        }

        catch(Exception e){
            e.printStackTrace();
        }

        return new KakaoTokenDto();
    }

    /* kakaoAccessToken 으로 카카오 서버에 정보 요청 */
    public Account getKakaoInfo(String kakaoAccessToken) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        // POST 방식으로 API 서버에 요청 보내고, response 받아옴
        try{

            ResponseEntity<String> accountInfoResponse = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    accountInfoRequest,
                    String.class
            );

            log.debug("카카오 서버에서 정상적으로 데이터를 수신했습니다.");

            // JSON Parsing (-> kakaoAccountDto)
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            KakaoAccountDto kakaoAccountDto = null;

            try {
                kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
            }

            catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // kakaoAccountDto 에서 필요한 정보 꺼내서 Account 객체로 매핑
            assert kakaoAccountDto != null;
            String email = kakaoAccountDto.getKakaoAccount().getEmail();
            String name = kakaoAccountDto.getKakaoAccount().getProfile().getNickname();
            log.debug("email: {}", email);
            log.debug("name: {}", name);

            return Account.builder()
                    .loginType("KAKAO")
                    .email(email)
                    .name(name)
                    .authority(Authority.ROLE_USER)
                    .build();
        }

        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /* login 요청 보내는 회원가입 유무 판단해 분기 처리 */
    public ResponseEntity<LoginResponseDto> kakaoLogin(String kakaoAccessToken) {
        // kakaoAccessToken 으로 회원정보 받아오기
        Account account = getKakaoInfo(kakaoAccessToken);
        log.debug("account(email): {}", account.getEmail());

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setKakaoAccessToken(kakaoAccessToken);
        loginResponseDto.setAccount(account);
        log.debug("loginResponseDto: {}", loginResponseDto);

        try {
            //로그인 성공
            TokenDto tokenDto = securityService.login(account.getEmail());
            log.debug("tokenDto: {}", tokenDto);

            Account dbAccount = kakaoAccountRepository.findByEmail(account.getEmail()).orElseThrow();
            log.debug("productService.findByPostUserId(account.getAccountId()).orElseThrow(): {}", productService.findByPostUserId(dbAccount.getId()).orElseThrow());
            log.debug("productService.findByRentUserId(account.getAccountId()).orElseThrow(): {}", productService.findByRentUserId(dbAccount.getId()).orElseThrow());

            HttpHeaders headers = setTokenHeaders(tokenDto);

            loginResponseDto.setLoginSuccess(true);
            loginResponseDto.getAccount().setId(dbAccount.getId());
            List<Product> lendProductList = productService.findByPostUserId(dbAccount.getId()).orElseThrow();
            List<ProductDto> lendItemDtoList = new ArrayList<>();
            lendItemDtoList = imagePathSetting(lendProductList, lendItemDtoList);

            for(ProductDto productDto : lendItemDtoList){
                productDto.setPostUserName(accountService.findById(productDto.getPostUserId()).orElseThrow().getName());
                productDto.setPostUserEmail(accountService.findById(productDto.getPostUserId()).orElseThrow().getEmail());
            }

            loginResponseDto.setLendItem(lendItemDtoList);
            List<Product> rentProductList = productService.findByRentUserId(dbAccount.getId()).orElseThrow();
            List<ProductDto> rentItemDtoList = new ArrayList<>();
            rentItemDtoList = imagePathSetting(rentProductList, rentItemDtoList);
            for(ProductDto productDto : rentItemDtoList){
                productDto.setPostUserName(accountService.findById(productDto.getPostUserId()).orElseThrow().getName());
                productDto.setPostUserEmail(accountService.findById(productDto.getPostUserId()).orElseThrow().getEmail());
            }

            loginResponseDto.setRentItem(rentItemDtoList);

            log.debug("getPostedProducts : {}", loginResponseDto.getLendItem());
            log.debug("getRentedProducts : {}", loginResponseDto.getRentItem());
            log.debug("return할 값: {}", ResponseEntity.ok().headers(headers).body(loginResponseDto));

            return ResponseEntity.ok().headers(headers).body(loginResponseDto);
        }

        catch (CEmailLoginFailedException e) {
            //로그인 실패
            loginResponseDto.setLoginSuccess(false);
            log.debug("계정 정보 못 찾음");
            kakaoAccountRepository.save(account);
            log.debug("DB에 계정 저장");
            return ResponseEntity.ok(loginResponseDto);
        }

    }//endKakaoLogin

    /* 토큰을 헤더에 배치 */
    public HttpHeaders setTokenHeaders(TokenDto tokenDto) {
        HttpHeaders headers = new HttpHeaders();
        ResponseCookie cookie = ResponseCookie.from("RefreshToken", tokenDto.getRefreshToken())
                .path("/")
                .maxAge(60*60*24*7) // 쿠키 유효기간 7일로 설정했음
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        headers.add("Set-cookie", cookie.toString());
        headers.add("Authorization", tokenDto.getAccessToken());

        return headers;
    }

    List<ProductDto> imagePathSetting(List<Product> productList, List<ProductDto> productDtoList){
        for (Product product : productList) {
            ProductDto productDto = new ProductDto(product);
            List<String> imagePathList = new ArrayList<>(3);
            for (int j = 0; j < 3; j++) {
                String imagePath = productDto.getImagePath().get(j);
                if (imagePath != null) {
                    imagePathList.add(imagePath);
                }
            }//endForJ
            productDto.setImagePath(imagePathList);
            productDtoList.add(productDto);
        }//endForI
        return productDtoList;
    }//endFunction

}//endClass