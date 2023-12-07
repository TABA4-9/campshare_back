package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Dto.*;
import TABA4_9.CampShare.Entity.*;
import TABA4_9.CampShare.Repository.AccountRepository;
import TABA4_9.CampShare.Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.String.valueOf;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final DanawaService danawaService;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ViewLogService viewLogService;
    private final FlaskService flaskService;
    private final AccountService accountService;

    /*
        인기 상품 3개 return 해주는 getThreeProduct()
    */
    @GetMapping("/product/data/main")
    public List<Product> getThreeProduct() {
        List<Product> product = new ArrayList<>(3);
        product.add(productService.findById(1L).orElseThrow());
        product.add(productService.findById(2L).orElseThrow());
        product.add(productService.findById(3L).orElseThrow());
        return product;
    }

    /*
        모든 상품 정보를 return 해주는 getAllProduct()
    */
    @GetMapping("/product/data/category")
    public List<Product> getAllProduct() {
        return productService.findAll().orElseThrow();
    }

    @PostMapping("/product/matching")
    public List<Product> matching(@RequestBody MatchingDto matchingDto){
        Product product = productService.findById(matchingDto.getProductId()).orElseThrow();
        product.setIsRented(true);
        product.setRentUserId(matchingDto.getRentUserId());

        Account dbAccount = accountService.findById(matchingDto.getRentUserId()).orElseThrow();
        log.info("matching dbAccount: {}", dbAccount);
        try{
            productService.save(product);
            List<Product> rentItem = productService.findByRentUserId(dbAccount.getId()).orElseThrow();
            log.info("Updated rentItem: {}", rentItem);
            return rentItem;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }//endMatching


    /*
        상품 업로드 - 1페이지    
    */
    @PostMapping("/post/nextPage")
    public String postProduct1(@RequestBody Product product) {
        Long headCount = Long.parseLong(product.getHeadcount().substring(0, 1));
//        System.out.println("headCount = " + headCount);
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
    public ResponseEntity<Product> postProduct2(@ModelAttribute PostProductDto postProductDto) {
        MultipartFile[] uploadFiles = postProductDto.getImage();

        Product product = new Product(postProductDto);
        product.setTimestamp(setTimeStamp());

        //todo 게시자 id 등록하기
        //product.setPostUserId();
        ProductImage productImage = new ProductImage();
        List<UploadResultDto> resultDtoList = new ArrayList<>();
        log.debug("Upload Files={}", (Object) uploadFiles);

        for (MultipartFile uploadFile : uploadFiles) {
            // 이미지 파일만 업로드 가능

            if (!Objects.requireNonNull(uploadFile.getContentType()).startsWith("image")) {
                // 이미지가 아닌경우 403 Forbidden 반환
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            log.debug("Original Name={}", originalName);

            assert originalName != null;
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            log.debug("File Name={}", fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "~" + fileName;
            log.debug("Save Name={}", saveName);

            Path savePath = Paths.get(saveName);
            log.debug("Save Path={}", savePath);

            productImage.setUuid(uuid);
            productImage.setImagePath(valueOf(savePath));
            product.setImagePath(valueOf(savePath));

            log.debug("ProductImage : {}", productImage);
            log.info("Tibero 저장 시도");
            try {
                productService.save(product);
                productImage.setProduct(product);
                productImageService.save(productImage);
                log.info("Tibero 저장 성공");
                uploadFile.transferTo(savePath);// 로컬에 이미지 저장
                resultDtoList.add(new UploadResultDto(fileName, uuid, folderPath));

                log.debug("ResultDtoList (in try):{}", resultDtoList);

            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }//endFor
        return new ResponseEntity<>(HttpStatus.OK);
    }//endSubmit

    /*
        상품 정보 수정
    */
    @PostMapping("/product/update")
    public UpdateDto updateProduct(@RequestBody Product updatedProduct) {

        UpdateDto updateDto = new UpdateDto();

        try {
            log.debug("수정 전: {}", productService.findById(updatedProduct.getId()));
            productService.save(updatedProduct);
            log.debug("수정 후: {}", productService.findById(updatedProduct.getId()));
            updateDto.setUpdateSuccess(true);
            updateDto.setUpdateProduct(updatedProduct);
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

//        /* Flask 통신 */
//        FlaskTestDto flaskTestDto = new FlaskTestDto();
//        flaskTestDto.setId(product.getId());
//        flaskTestDto.setName(product.getName());
//
//        List<Product> recommendProducts = sendToFlask(flaskTestDto);
//
//        showProducts.add(recommendProducts.get(0));
//        showProducts.add(recommendProducts.get(1));
//        showProducts.add(recommendProducts.get(2));

        /* 추천 상품 정보 반환 */
        return showProducts;
    }

    @GetMapping("/product/data/search")
    public List<Product> searchProduct(@RequestParam String searchInput){
        return productService.findByNameLike(searchInput).orElseThrow();
    }


    /* functions */

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

    String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        log.debug("Data str : {}", str);

        String folderPath = str.replace("/", File.separator);
        log.debug("Folder Path : {}", folderPath);

        // make folder
        File uploadPatheFolder = new File(uploadPath, folderPath);

        if (!uploadPatheFolder.exists()) {
            uploadPatheFolder.mkdirs();
        }
        return folderPath;
    }

//    public List<Product> sendToFlask(FlaskTestDto flaskTestDto) throws JsonProcessingException {
//        RecommendItemDto recommendItemDto = flaskService.sendToFlask(flaskTestDto);
//
//        Long item1 = recommendItemDto.getRecommendItemId1();
//        Long item2 = recommendItemDto.getRecommendItemId2();
//        Long item3 = recommendItemDto.getRecommendItemId3();
//
//        List<Product> products = new ArrayList<>();
//
//        products.add(productService.findById(item1).orElseThrow());
//        products.add(productService.findById(item2).orElseThrow());
//        products.add(productService.findById(item3).orElseThrow());
//        return products;
//    }

}//endClass