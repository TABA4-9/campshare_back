package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Dto.Product.*;
import TABA4_9.CampShare.Entity.Account;
import TABA4_9.CampShare.Entity.Danawa;
import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Entity.ViewLog;
import TABA4_9.CampShare.Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        productDtoList = imagePathSetting(productList, productDtoList);

        return productDtoList;
    }

    /*
        모든 상품 정보를 return 해주는 getAllProduct()
    */
    @GetMapping("/product/data/category")
    public List<ProductDto> getAllProduct() {
        List<Product> productList = productService.findAll().orElseThrow();
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList = imagePathSetting(productList, productDtoList);

        return productDtoList;
    }

    @PostMapping("/product/matching")
    public MatchingDto matching(@RequestBody MatchingDto matchingDto) {
        //product id로 상품 찾기
        Product product = productService.findById(matchingDto.getProductId()).orElseThrow();
        product.setIsRented(true);
        product.setRentUserId(matchingDto.getRentUserId());

        //rentuserid로 account 찾기
        Account dbAccount = accountService.findById(matchingDto.getRentUserId()).orElseThrow();
        log.debug("matching dbAccount: {}", dbAccount);

        //product update
        productService.save(product);

        //account의 rent product 찾기
        List<Product> productList = productService.findByRentUserId(dbAccount.getId()).orElseThrow();
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList = imagePathSetting(productList, productDtoList);
        matchingDto.setRentItem(productDtoList);
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

    /*
        상품 업로드 - 2페이지
    */
    @PostMapping("/post/submit")
    public ResponseEntity<UploadResultDto> postProduct2(@ModelAttribute PostProductDto postProductDto) throws IOException {
        MultipartFile[] uploadFiles = postProductDto.getImage();
        List<String> imagePath = new ArrayList<>();
        UploadResultDto uploadResultDto = new UploadResultDto();
        Product product = new Product(postProductDto);
        product.setTimestamp(setTimeStamp());

        log.debug("Upload Files={}", (Object) uploadFiles);
        for (MultipartFile uploadFile : uploadFiles) {
            // 이미지 파일만 업로드 가능

            if (!Objects.requireNonNull(uploadFile.getContentType()).startsWith("image")) {
                // 이미지가 아닌경우 403 Forbidden 반환
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String url = s3UploadService.saveFile(uploadFile);
            imagePath.add(url);
            log.debug("Return Url = {}", url);
        }//endFor

        for(int i=0; imagePath.listIterator(i).hasNext(); i++){
            switch (i){
                case 0: product.setImagePath1(imagePath.get(i)); break;
                case 1: product.setImagePath2(imagePath.get(i)); break;
                case 2: product.setImagePath3(imagePath.get(i));
            }
        }//endFor

        log.debug("Tibero 저장 시도");
        productService.save(product);
        log.debug("Tibero 저장 성공");

        //rentuserid로 account 찾기
        Account dbAccount = accountService.findById(product.getPostUserId()).orElseThrow();
        log.debug("matching dbAccount: {}", dbAccount);

        //account의 lend product 찾기
        List<Product> productList = productService.findByPostUserId(dbAccount.getId()).orElseThrow();
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList = imagePathSetting(productList, productDtoList);

        uploadResultDto.setLendItem(productDtoList);
        log.debug("Updated rentItem: {}", uploadResultDto.getLendItem());

        return new ResponseEntity<>(uploadResultDto, HttpStatus.OK);
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
        }catch (Exception e) {
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
    //@PostMapping("/detail/{itemId}")
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
    public List<ProductDto> searchProduct(@RequestParam String searchInput){
        List<Product> productList = productService.findByNameLike(searchInput).orElseThrow();
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList = imagePathSetting(productList, productDtoList);

        return productDtoList;
    }


    /* functions */
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