package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Dto.*;
import TABA4_9.CampShare.Entity.PostProduct;
import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Entity.ProductImage;
import TABA4_9.CampShare.Service.PostProductService;
import TABA4_9.CampShare.Service.ProductImageService;
import TABA4_9.CampShare.Service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;
    @GetMapping("/product/data/main")
    public List<Product> getThreeProduct(){

        Optional<Product> product1 = productService.findById(1L);
        Optional<Product> product2 = productService.findById(27L);
        Optional<Product> product3 = productService.findById(28L);

        List<Product> product = new ArrayList<>(3);

        product.add(product1.orElseThrow());
        product.add(product2.orElseThrow());
        product.add(product3.orElseThrow());

        return product;
    }

    @GetMapping("/product/data/category")
    public List<Product> getAllProduct(){
        List<Product> productList = productService.findAll();
        log.debug("Product List : {}", productList.toString());
        return productList;
    }

    @Value("${image.upload.path}") // application.properties의 변수
    private String uploadPath;
    @ExceptionHandler
    @PostMapping("/post/nextPage")
    protected String postProduct1(@RequestBody Product product, Exception e){
        return minPrice(product);
    }

    @PostMapping("/post/submit")
    public ResponseEntity<Product> postProduct2(@ModelAttribute PostProductDto postProductDto){
        MultipartFile[] uploadFiles = postProductDto.getImage();

        Product product = new Product();
        product.setId(postProductDto.getId());
        product.setName(postProductDto.getName());
        product.setCategory(postProductDto.getCategory());
        product.setHeadcount(postProductDto.getHeadcount());
        product.setExplanation(postProductDto.getExplanation());
        product.setPrice(postProductDto.getPrice());
        product.setBrand(postProductDto.getBrand());
        product.setUsingYear(postProductDto.getUsingYear());
        product.setPeriod(postProductDto.getPeriod());
        product.setAddress(postProductDto.getAddress());
        product.setUserID(postProductDto.getUserID());
        product.setIsRented(postProductDto.getIsRented());
        product.setTimestamp(setTimeStamp());


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
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + fileName;
            log.debug("Save Name={}", saveName);

            Path savePath = Paths.get(saveName);
            log.debug("Save Path={}", savePath);

            productImage.setUuid(uuid);
            productImage.setImagePath(String.valueOf(savePath));
            product.setImagePath(String.valueOf(savePath));

            log.debug("ProductImage : {}", productImage);

            try {
                log.debug("Tibero 저장 시도");
                productImageService.save(productImage);
                productService.save(product);
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

    @PostMapping("/upload")
    public ResponseEntity<List<UploadResultDto>> uploadFile(@RequestPart MultipartFile[] uploadFiles){
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
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + fileName;
            log.debug("Save Name={}", saveName);

            Path savePath = Paths.get(saveName);
            log.debug("Save Path={}", savePath);

            productImage.setUuid(uuid);
            productImage.setImagePath(String.valueOf(savePath));
            log.debug("ProductImage : {}", productImage);

            try {
                log.debug("Tibero 저장 시도");
                productImageService.save(productImage);
                log.debug("Tibero 저장 성공");

                uploadFile.transferTo(savePath);// 실제 이미지 저장
                resultDtoList.add(new UploadResultDto(fileName, uuid, folderPath));

                log.debug("ResultDtoList (in try):{}", resultDtoList);

            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }//endFor
        log.debug("ResultDtoList (before return):{}", resultDtoList);
        return new ResponseEntity<>(resultDtoList, HttpStatus.OK);
    }//endMethod

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

    @PostMapping("/detail/{id}")
    public Optional<Product> detailProduct(@PathVariable("id") Long productId, @RequestBody DetailDto detailDto) {
        log.debug("productId = {}, userId = {}, detailPageLog = {}", productId, detailDto.getUserId(), detailDto.getDetailPageLog());

        Optional<Product> product = productService.findById(productId);

        return Optional.of(product.orElseThrow());
    }

    public String minPrice(Product product){
        return "서비스 준비 중";
    }

    public String setTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String makeFolder() {

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