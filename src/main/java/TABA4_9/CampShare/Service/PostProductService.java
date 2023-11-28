package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Entity.PostProduct;
import TABA4_9.CampShare.Repository.PostProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PostProductService {


    final PostProductRepository postProductRepository ;
    public PostProduct save(PostProduct postProduct){
        return postProductRepository.save(postProduct);
    }
    public Optional<PostProduct> findById(Long id){
        return postProductRepository.findById(id);
    }

    public Long saveGetId(PostProduct postProduct){
        return postProductRepository.save(postProduct).getId();
    }

    public void delete(PostProduct  postProduct){
        postProductRepository.delete(postProduct);
    }


}
