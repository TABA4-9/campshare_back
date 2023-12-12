package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Dto.Product.*;
import TABA4_9.CampShare.Entity.Account;
import TABA4_9.CampShare.Entity.Danawa;
import TABA4_9.CampShare.Entity.Product;
import TABA4_9.CampShare.Entity.ViewLog;
import TABA4_9.CampShare.Service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final DanawaService danawaService;
    private final ProductService productService;
    private final AccountService accountService;
    private final S3UploadService s3UploadService;
    private final ViewLogService viewLogService;

    /*
        랜덤 상품 3개 return 해주는 getThreeProduct()
    */
    @GetMapping("/product/data/main")
    public List<ProductDto> getThreeProduct() {
        List<Product> allProductList = productService.findAll().orElseThrow();
        List<Product> threeProductList = new ArrayList<>(3);
        List<ProductDto> productDtoList = new ArrayList<>(3);

        Set<Integer> uniqueRandomSet = new HashSet<>();

        while (uniqueRandomSet.size() < 3) {
            double randomDouble = Math.random();
            int randomToInt = (int)(randomDouble * allProductList.size());
            uniqueRandomSet.add(randomToInt);
        }
        Integer[] randomArray = uniqueRandomSet.toArray(new Integer[0]);

        for(int i=0; i<3; i++){
            threeProductList.add(allProductList.get(randomArray[i]));
        }

        productDtoList = imagePathSetting(threeProductList, productDtoList);
        for(ProductDto productDto : productDtoList){
            productDto.setPostUserName(accountService.findById(productDto.getPostUserId()).orElseThrow().getName());
            productDto.setPostUserEmail(accountService.findById(productDto.getPostUserId()).orElseThrow().getEmail());
        }
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
        for(ProductDto productDto : productDtoList){
            productDto.setPostUserName(accountService.findById(productDto.getPostUserId()).orElseThrow().getName());
            productDto.setPostUserEmail(accountService.findById(productDto.getPostUserId()).orElseThrow().getEmail());
        }
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
        //
        String startDateStr = product.getStartDate();
        String endDateStr = product.getEndDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        double avgPrice = avgPrice(danawaService.findByPeople(headCount)) * daysBetween;//WHERE=몇인용


        //
        if (avgPrice == 0L) {
            return "추천 가격 정보가 없습니다";
        } else {
            double usingYear = (Long.parseLong(product.getUsingYear().substring(0, 1)));
            return String.format("%.0f", (1- (usingYear / 10)) * avgPrice * 0.2); //감가상각 수식 적용
        }
    }//endNextPage
    /*
        상품 정보 수정
    */
    @PostMapping("/product/update")
    public ResponseEntity<UpdateDto> updateProduct(@ModelAttribute PostProductDto updatedProduct) {
        UpdateDto updateDto = new UpdateDto();
        MultipartFile[] uploadFiles = updatedProduct.getImage();
        List<String> imagePath = new ArrayList<>();
        List<String> imageUrl = updatedProduct.getImageUrl();
        Product product = new Product(updatedProduct);
        product.setTimestamp(setTimeStamp());

        try {
            if(uploadFiles != null){
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
            }
            int i, j;
            for(i=0; imageUrl.listIterator(i).hasNext(); i++){
                switch (i){
                    case 0: product.setImagePath1(imageUrl.get(i)); break;
                    case 1: product.setImagePath2(imageUrl.get(i)); break;
                    case 2: product.setImagePath3(imageUrl.get(i));
                }
            }//endFor

            for(j=0; imagePath.listIterator(j).hasNext(); i++, j++){
                switch (i){
                    case 0: product.setImagePath1(imagePath.get(j)); break;
                    case 1: product.setImagePath2(imagePath.get(j)); break;
                    case 2: product.setImagePath3(imagePath.get(j));
                }
            }//endFor

            log.debug("수정 전: {}", productService.findById(updatedProduct.getId()));
            productService.save(product);
            log.debug("수정 후: {}", productService.findById(updatedProduct.getId()));
            updateDto.setUpdateSuccess(true);
        }catch (Exception e) {
            updateDto.setUpdateSuccess(false);
        }
        //postuserid로 account 찾기
        Account dbAccount = accountService.findById(product.getPostUserId()).orElseThrow();
        log.debug("matching dbAccount: {}", dbAccount);

        //account의 lend product 찾기
        List<Product> productList = productService.findByPostUserId(dbAccount.getId()).orElseThrow();
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList = imagePathSetting(productList, productDtoList);

        updateDto.setLendItem(productDtoList);
        log.debug("Updated rentItem: {}", updateDto.getLendItem());

        return new ResponseEntity<>(updateDto, HttpStatus.OK);
    }//endUpdate

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

        //postuserid로 account 찾기
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
        상품 정보 삭제
    */
    @PostMapping("/product/delete")
    public DeleteDto deleteProduct(@RequestBody DeleteDto deleteDto) {
        System.out.println("delete productId = " + deleteDto.getProductId());
        Product product = productService.findById(deleteDto.getProductId()).orElseThrow();
        List<ViewLog> viewLogList = viewLogService.deleteAllByItemId(product.getId()).orElseThrow();
        for(ViewLog viewLog : viewLogList){
            System.out.println("deleted viewLogItemId: " + viewLog.getItemId());
        }
        System.out.println("try 진입 전");
        try {
            productService.delete(product);
            System.out.println("product 삭제 후");
            //postuserid로 account 찾기
            Account dbAccount = accountService.findById(product.getPostUserId()).orElseThrow();
            log.debug("matching dbAccount: {}", dbAccount);

            //account의 lend product 찾기
            List<Product> productList = productService.findByPostUserId(product.getPostUserId()).orElseThrow();
            List<ProductDto> productDtoList = new ArrayList<>();
            productDtoList = imagePathSetting(productList, productDtoList);

            deleteDto.setLendItem(productDtoList);
            deleteDto.setDeleteSuccess(true);

        }
        catch (Exception e) {
            deleteDto.setDeleteSuccess(false);
        }

        return deleteDto;
    }//endDelete

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