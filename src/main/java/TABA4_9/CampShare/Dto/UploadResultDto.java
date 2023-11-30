package TABA4_9.CampShare.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Getter
@Setter
@AllArgsConstructor
public class UploadResultDto {

    private String fileName;

    private String uuid;

    private String folderPath;

    public String getImageURL(){
        try {
            return URLEncoder.encode(folderPath+"/" +uuid + fileName,"UTF-8");

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }
}