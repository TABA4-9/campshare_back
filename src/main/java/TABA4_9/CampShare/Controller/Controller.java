package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Entity.Entity;
import TABA4_9.CampShare.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@org.springframework.stereotype.Controller
public class Controller implements ErrorController{
    @Autowired
    private Service service;

    @GetMapping({"/", "/error"})
    public String index() {
        return "index.html";
    }

    @ResponseBody
    @PostMapping("/postData")
    public void postData(@RequestBody Entity entity){
        service.write(entity);
        System.out.println("Saved <name: " + entity.getName() + ", content: " + entity.getContent()+">");
    }

    @ResponseBody
    @GetMapping("/getData")
    public Entity getData(){
        Entity res = new Entity();
        res.setName("Spring Boot");
        res.setContent("server response");
        return res;
    }

}
