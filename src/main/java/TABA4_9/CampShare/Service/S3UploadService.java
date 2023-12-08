package TABA4_9.CampShare.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        // 날짜 폴더 생성
        String folderPath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        //UUID
        String uuid = UUID.randomUUID().toString();

        //저장할 파일 이름
        String saveName = folderPath + "/" + uuid + "~" + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, saveName, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, saveName).toString();
    }
}
