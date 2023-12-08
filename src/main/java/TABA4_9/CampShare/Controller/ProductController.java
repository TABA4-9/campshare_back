package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Dto.*;
import TABA4_9.CampShare.Entity.Account;
import TABA4_9.CampShare.Entity.Danawa;
import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Entity.ViewLog;
import TABA4_9.CampShare.Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final DanawaService danawaService;
    private final ProductService productService;
    private final ViewLogService viewLogService;
    private final AccountService accountService;
    private final S3UploadService s3UploadService;
    /*
        인기 상품 3개 return 해주는 getThreeProduct()
    */
    @GetMapping("/product/data/main")
    public List<ProductDto> getThreeProduct() {
        List<Product> productList = new ArrayList<>(3);
        List<ProductDto> productDtoList = new ArrayList<>(3);
        productList.add(productService.findById(1L).orElseThrow());
        productList.add(productService.findById(2L).orElseThrow());
        productList.add(productService.findById(3L).orElseThrow());
        imagePathSetting(productList, productDtoList);

        return productDtoList;
    }

    /*
        모든 상품 정보를 return 해주는 getAllProduct()
    */
    @GetMapping("/product/data/category")
    public List<ProductDto> getAllProduct() {
        List<Product> productList = productService.findAll().orElseThrow();
        List<ProductDto> productDtoList = new ArrayList<>();
        imagePathSetting(productList, productDtoList);
        return productDtoList;
    }

    @PostMapping("/product/matching")
    public MatchingDto matching(@RequestBody MatchingDto matchingDto) {
        Product product = productService.findById(matchingDto.getProductId()).orElseThrow();
        product.setIsRented(true);
        product.setRentUserId(matchingDto.getRentUserId());

        Account dbAccount = accountService.findById(matchingDto.getRentUserId()).orElseThrow();
        log.debug("matching dbAccount: {}", dbAccount);
        productService.save(product);
        matchingDto.setRentItem(productService.findByRentUserId(dbAccount.getId()).orElseThrow());
        log.debug("Updated rentItem: {}", matchingDto.getRentItem());
        return matchingDto;
    }//endMatching


    /*
        상품 업로드 - 1페이지    
    */
    @ExceptionHandler
    @PostMapping("/post/nextPage")
    public String postProduct1(@RequestBody Product product, Exception e) {
        Long headCount = Long.parseLong(product.getHeadcount().substring(0, 1));
        double avgPrice = avgPrice(danawaService.findByPeople(headCount)); //WHERE=몇인용

        if (avgPrice == 0L) {
            return "추천 가격 정보가 없습니다";
        } else {
            double usingYear = (Long.parseLong(product.getUsingYear().substring(0, 1)));
            return String.format("%.0f", (usingYear / 10) * avgPrice * 0.02); //감가상각 수식 적용
        }
    }//endNextPage

    @Value("${image.upload.path}") // application.properties의 변수
    private String uploadPath;

    /*
        상품 업로드 - 2페이지
    */
    @PostMapping("/post/submit")
    public ResponseEntity<Product> postProduct2(@ModelAttribute PostProductDto postProductDto) throws IOException {
        MultipartFile[] uploadFiles = postProductDto.getImage();
        Product product = new Product(postProductDto);
        product.setTimestamp(setTimeStamp());

        List<UploadResultDto> resultDtoList = new ArrayList<>();
        log.debug("Upload Files={}", (Object) uploadFiles);

        for (MultipartFile uploadFile : uploadFiles) {
            // 이미지 파일만 업로드 가능

            if (!Objects.requireNonNull(uploadFile.getContentType()).startsWith("image")) {
                // 이미지가 아닌경우 403 Forbidden 반환
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String url = s3UploadService.saveFile(uploadFile);
            product.setImagePath1(url);

            log.info("Return Url = {}", url);

            log.debug("Tibero 저장 시도");
            productService.save(product);
            log.debug("Tibero 저장 성공");
        }//endFor
        return new ResponseEntity<>(HttpStatus.OK);
    }//endSubmit

    /*
        상품 정보 수정
    */
    @PostMapping("/product/update")
    public UpdateDto updateProduct(@ModelAttribute PostProductDto updatedProduct) {

        UpdateDto updateDto = new UpdateDto();

        try {
            log.debug("수정 전: {}", productService.findById(updatedProduct.getId()));
            Product updatingProduct = new Product(updatedProduct);
            updatingProduct.setTimestamp(setTimeStamp());
            productService.save(updatingProduct);
            log.debug("수정 후: {}", productService.findById(updatedProduct.getId()));
            updateDto.setUpdateSuccess(true);
            updateDto.setUpdateProduct(updatingProduct);
        } catch (Exception e) {
            updateDto.setUpdateSuccess(false);
            updateDto.setUpdateProduct(null);
        }

        return updateDto;
    }//endUpdate

    /*
        상품 정보 삭제
    */
    @PostMapping("/product/delete")
    public DeleteDto deleteProduct(@RequestBody DeleteDto deleteDto) {
        System.out.println("delete productId = " + deleteDto.getProductId());

        try {
            productService.delete(productService.findById(deleteDto.getProductId()).orElseThrow());
            deleteDto.setDeleteSuccess(true);
        }
        catch (Exception e) {
            deleteDto.setDeleteSuccess(false);
        }

        return deleteDto;
    }//endDelete

    /*
        개별 상품 페이지
    */
    @PostMapping("/detail/{itemId}")
    public List<Product> detailProduct(@PathVariable("itemId") Long itemId, @RequestBody DetailDto detailDto) throws JsonProcessingException {
        List<Product> showProducts = new ArrayList<>();
        Product product = productService.findById(itemId).orElseThrow();

        /* 로그 기록 */
        ViewLog viewLog = new ViewLog();
        viewLog.setItemId(product.getId());
        viewLog.setUserId(detailDto.getUserId());
        viewLog.setTimeStamp(detailDto.getDetailPageLog());
        viewLogService.save(viewLog);

        /* 추천 상품 정보 반환 */
        return showProducts;
    }

    @GetMapping("/product/data/search")
    public List<Product> searchProduct(@RequestParam String searchInput){
        return productService.findByNameLike(searchInput).orElseThrow();
    }


    /* functions */
    void imagePathSetting(List<Product> productList, List<ProductDto> productDtoList){
        for(int i =0; i<productDtoList.size(); i++){
            ProductDto productDto = new ProductDto(productList.get(i));
            List<String> imagePathList = new ArrayList<>(3);
            for(int j=0; j<3; j++){
                String imagePath = productDto.getImagePath().get(j);
                if(imagePath == null){
                    continue;
                }
                imagePathList.add(imagePath);
            }//endForJ
            productDto.setImagePath(imagePathList);
            productDtoList.add(productDto);
        }//endForI
    }//endFunction

    Long avgPrice(Optional<List<Danawa>> danawaList) {
        Long avg = 0L;
        Long count = 0L;
        for (Danawa danawa : danawaList.orElseThrow()) {
            avg += danawa.getPrice();
            count++;
        }
        if (count == 0) {
            return 0L;
        } else {
            return (avg / count);
        }
    }

    String setTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}//endClass