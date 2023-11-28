package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Entity.PostProduct;
import TABA4_9.CampShare.Repository.PostProductInterface;
import TABA4_9.CampShare.Repository.PostProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PostProductService {

    @Autowired
    PostProductInterface postProductInterface;
    public PostProduct write(PostProduct postProduct){
        return postProductInterface.save(postProduct);
    }
    public Optional<PostProduct> find(int id){
        return postProductInterface.findById(id);
    }

    public void remove(PostProduct  postProduct){
        postProductInterface.delete(postProduct);
    }


}
