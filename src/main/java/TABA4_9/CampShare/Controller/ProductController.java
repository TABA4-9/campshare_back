package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Dto.*;
import TABA4_9.CampShare.Entity.Danawa;
import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Entity.ProductImage;
import TABA4_9.CampShare.Service.DanawaService;
import TABA4_9.CampShare.Service.ProductImageService;
import TABA4_9.CampShare.Service.ProductService;
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

    private final SearchLogController searchLogController;

    /*
        인기 상품 3개 return 해주는 getThreeProduct()
    */
    @GetMapping("/product/data/main")
    public List<Product> getThreeProduct(){

        Optional<Product> product1 = productService.findById(2L);
        Optional<Product> product2 = productService.findById(3L);
        Optional<Product> product3 = productService.findById(6L);

        List<Product> product = new ArrayList<>(3);

        product.add(product1.orElseThrow());
        product.add(product2.orElseThrow());
        product.add(product3.orElseThrow());

        return product;
    }

    /*
        모든 상품 정보를 return 해주는 getAllProduct()
    */
    @GetMapping("/product/data/category")
    public List<Product> getAllProduct(){
        List<Product> productList = productService.findAll().orElseThrow();
        log.debug("Product List : {}", productList);
        return productList;
    }
    
    /*
        상품 업로드 - 1페이지    
    */
    @ExceptionHandler
    @PostMapping("/post/nextPage")
    protected String postProduct1(@RequestBody Product product, Exception e){
        Long headCount = Long.parseLong(product.getHeadcount());
        double returnPrice = recommendPrice(danawaService.findByPeople(headCount));
        if (returnPrice == 0L) {
            return "추천 가격 정보가 없습니다";
        } else {
            double usingYear = (Long.parseLong(product.getUsingYear().substring(0,1)));
            String resultRecommendPrice = String.format("%.2f",(usingYear / 10) * returnPrice * 0.05);
            return resultRecommendPrice;
        }
    }

    public Long recommendPrice(Optional<List<Danawa>> danawas) {
        Long avg = 0L;
        Long count = 0L;
        for (Danawa danawa : danawas.orElseThrow()) {
            avg = avg + danawa.getPrice();
            count++;
        }
        if (count == 0) {
            return 0L;
        } else {
            return (avg / count);
        }
    }



    @Value("${image.upload.path}") // application.properties의 변수
    private String uploadPath;
    
    /*
        상품 업로드 - 2페이지
    */
    @PostMapping("/post/submit")
    public ResponseEntity<Product> postProduct2(@ModelAttribute PostProductDto postProductDto){
        MultipartFile[] uploadFiles = postProductDto.getImage();

        Product product = new Product(postProductDto);
        product.setTimestamp(setTimeStamp());
        product.setIsRented(false);
        //게시자 id 등록하기 product.setPostUserId();
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

            try {
                log.debug("Tibero 저장 시도");
                productService.save(product);
                productImage.setProduct(product);
                productImageService.save(productImage);
                log.debug("Tibero 저장 성공");

                uploadFile.transferTo(savePath);// 실제 이미지 저장
                resultDtoList.add(new UploadResultDto(fileName, uuid, folderPath));

                log.debug("ResultDtoList (in try):{}", resultDtoList);

            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }//endFor
        return new ResponseEntity<>(HttpStatus.OK);
    }//endMethod

    /*
        상품 정보 수정
    */
    @PutMapping("/product/update")
    public UpdateDto updateProduct(@RequestBody Product updatedProduct){

        UpdateDto updateDto = new UpdateDto();

        try{
            log.debug("수정 전: {}", productService.findById(updatedProduct.getId()));
            productService.save(updatedProduct);
            log.debug("수정 후: {}", productService.findById(updatedProduct.getId()));
            updateDto.setUpdateSuccess(true);
            updateDto.setUpdateProduct(updatedProduct);

        }

        catch(Exception e){
            updateDto.setUpdateSuccess(false);
            updateDto.setUpdateProduct(null);
        }

        return updateDto;
    }

    /*
        상품 정보 삭제
    */
    @DeleteMapping("/product/delete")
    public DeleteDto deleteProduct(@RequestParam Long productId){

        DeleteDto deleteDto = new DeleteDto();

        try{
            productService.delete(productService.findById(productId).orElseThrow());
            deleteDto.setDeleteSuccess(true);
            deleteDto.setProductId(productId);
        }

        catch(Exception e){
            deleteDto.setDeleteSuccess(false);
            deleteDto.setProductId(productId);
        }

        return deleteDto;
    }
    
    /*
        개별 상품 페이지
    */
    @PostMapping("/detail/{itemId}")
    public Optional<Product> detailProduct(@PathVariable("itemId") Long itemId, @RequestBody DetailDto detailDto) {
//        log.debug("productId = {}, userId = {}, detailPageLog = {}", itemId, detailDto.getUserId(), detailDto.getDetailPageLog());

        Optional<Product> product = productService.findById(itemId);
        searchLogController.saveLog(product, detailDto.getUserId(), detailDto.getDetailPageLog());

        return Optional.of(product.orElseThrow());
    }




    String recommendPrice(Product product) {

        product.getPrice();
        return "서비스 준비 중";
    }

    public static String setTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        log.debug("Data str : {}", str);

        String folderPath = str.replace("/", File.separator);
        log.debug("Folder Path : {}", folderPath);

        // make folder
        File uploadPatheFolder = new File(uploadPath, folderPath);

        if(!uploadPatheFolder.exists()){
            uploadPatheFolder.mkdirs();
        }
        return folderPath;
    }

}//endClass