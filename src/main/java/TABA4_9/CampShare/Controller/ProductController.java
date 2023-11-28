package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Entity.ProductImage;
import TABA4_9.CampShare.Entity.UploadResultDto;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;
    @GetMapping("/product/data/main")
    public Product[] getThreeProduct(){

        Optional<Product> product1 = productService.findById(1L);
        Optional<Product> product2 = productService.findById(2L);
        Optional<Product> product3 = productService.findById(3L);

        Product[] product = new Product[3];
        product[0] = product1.orElseGet(() -> null);
        product[1] = product2.orElseGet(() -> null);
        product[2] = product3.orElseGet(() -> null);
        return product;
    }

    @GetMapping("/product/data/category")
    public Product[] getAllProduct(){
        List<Product> itemList = productService.findAll();
        Product[] product = new Product[itemList.size()];
        itemList.toArray(product);
        log.info("Product List : {}", Arrays.toString(product));
        return product;

    }

    @Value("${image.upload.path}") // application.properties의 변수
    private String uploadPath;

    @PostMapping("/upload")
    public ResponseEntity<List<UploadResultDto>> uploadFile(@RequestPart MultipartFile[] uploadFiles){
        int length = uploadFiles.length;
        ProductImage productImage = new ProductImage();
        List<UploadResultDto> resultDtoList = new ArrayList<>();
        log.info("Upload Files={}", (Object) uploadFiles);

        for (int i=0; i<uploadFiles.length; i++) {
            log.info("enter for loop");
            // 이미지 파일만 업로드 가능

            if(!Objects.requireNonNull(uploadFiles[i].getContentType()).startsWith("image")){
                // 이미지가 아닌경우 403 Forbidden 반환
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFiles[i].getOriginalFilename();
            log.info("Original Name={}", originalName);

            assert originalName != null;
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            log.info("File Name={}", fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + fileName;
            log.info("Save Name={}", saveName);

            Path savePath = Paths.get(saveName);
            log.info("Save Path={}", savePath);

            productImage.setUuid(uuid);
            productImage.setImagePath(String.valueOf(savePath));
            log.info("ProductImage : {}", productImage);
            try {
                log.info("Tibero 저장 시도");
                productImageService.save(productImage);
                log.info("Tibero 저장 성공");

                uploadFiles[i].transferTo(savePath);// 실제 이미지 저장
                resultDtoList.add(new UploadResultDto(fileName, uuid, folderPath));

                log.info("ResultDtoList (in try):{}", resultDtoList);

            }catch (IOException e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }//endFor
        log.info("ResultDtoList (before return):{}", resultDtoList);
        return new ResponseEntity<>(resultDtoList, HttpStatus.OK);
    }//endMethod

    private String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        log.info("Data str : {}", str);

        String folderPath = str.replace("/", File.separator);
        log.info("Folder Path : {}", folderPath);

        // make folder
        File uploadPatheFolder = new File(uploadPath, folderPath);

        if(!uploadPatheFolder.exists()){
            uploadPatheFolder.mkdirs();
        }
        return folderPath;
    }
}