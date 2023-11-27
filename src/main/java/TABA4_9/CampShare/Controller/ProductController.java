package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Entity.UploadResultDto;
import TABA4_9.CampShare.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/product/data/main")
    public Product[] getThreeProduct(){
        Product[] product = new Product[3];
        product[0]=productService.find(1);
        product[1]=productService.find(2);
        product[2]=productService.find(3);
        return product;
    }

    @Value("${image.upload.path}") // application.properties의 변수
    private String uploadPath;

    @PostMapping("/upload")
    public ResponseEntity<List<UploadResultDto>> uploadFile(@RequestPart MultipartFile[] uploadFiles){

        List<UploadResultDto> resultDtoList = new ArrayList<>();
        System.out.println("Upload Files : " + uploadFiles);

        for (int i=0; i<uploadFiles.length; i++) {
            System.out.println("for문 진입");
            // 이미지 파일만 업로드 가능
            if(!Objects.requireNonNull(uploadFiles[i].getContentType()).startsWith("image")){
                // 이미지가 아닌경우 403 Forbidden 반환
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFiles[i].getOriginalFilename();
            System.out.println("Original Name: " + originalName);

            assert originalName != null;
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            System.out.println("File Name : " + fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + fileName;
            System.out.println("Save Name : " + saveName);

            Path savePath = Paths.get(saveName);
            System.out.println("Save Path : " + savePath);

            try {
                uploadFiles[i].transferTo(savePath);// 실제 이미지 저장
                resultDtoList.add(new UploadResultDto(fileName, uuid, folderPath));
                System.out.println("ResultDtoList (in try): " + resultDtoList);
            }catch (IOException e){
                e.printStackTrace();
            }
        }//endFor
        System.out.println("ResultDtoList (before return): " + resultDtoList);
        return new ResponseEntity<>(resultDtoList, HttpStatus.OK);
    }//endMethod

    private String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        System.out.println("Data str : " + str);

        String folderPath = str.replace("/", File.separator);
        System.out.println("Folder Path : " + folderPath);

        // make folder ----
        File uploadPatheFolder = new File(uploadPath, folderPath);

        if(!uploadPatheFolder.exists()){
            uploadPatheFolder.mkdirs();
        }

        return folderPath;
    }
}