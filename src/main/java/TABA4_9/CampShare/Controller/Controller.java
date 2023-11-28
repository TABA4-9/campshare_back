package TABA4_9.CampShare.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.File;

@RestController
public class Controller /*implements ErrorController*/{
    /*
    @GetMapping("/")
    public void index(){
        String path = "./image"; //폴더 경로
        File Folder = new File(path);

        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try{
                Folder.mkdir(); //폴더 생성합니다.
                System.out.println("폴더가 생성되었습니다.");
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }else {
            System.out.println("이미 폴더가 생성되어 있습니다.");
        }

    }
*/
/*
    private Service service;

    @GetMapping({"/", "/error"})
    public String index() {
        return "index.html";
    }

    @PostMapping("/postData")
    public void postData(@RequestBody Entity entity){
        service.write(entity);
        System.out.println("Saved <name: " + entity.getName() + ", content: " + entity.getContent()+">");
    }

    @GetMapping("/getData")
    public Entity getData(){
        Entity res = new Entity();
        res.setName("Spring Boot");
        res.setContent("server response");
        return res;
    }
*/

}//endClass
