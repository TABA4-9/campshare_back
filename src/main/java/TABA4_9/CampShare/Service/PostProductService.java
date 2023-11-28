package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Entity.PostProduct;
import TABA4_9.CampShare.Repository.PostProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostProductService {


    final PostProductRepository postProductRepository ;
    public PostProduct save(PostProduct postProduct){
        return postProductRepository.save(postProduct);
    }
    public Optional<PostProduct> find(int id){
        return postProductRepository.findById(id);
    }

    public void remove(PostProduct  postProduct){
        postProductRepository.delete(postProduct);
    }


}
