package TABA4_9.CampShare.Controller;

import TABA4_9.CampShare.Entity.PostProduct;
import TABA4_9.CampShare.Repository.PostProductRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class PostProductRepositoryController{
    private Map<Long, PostProduct> store = new HashMap<>();

    private static Long sequence = 0L;

    private static final PostProductRepositoryController instance = new PostProductRepositoryController();

    public static PostProductRepositoryController getInstance() {
        return instance;
    }

    private PostProductRepositoryController() {

    }

    public PostProduct saveID(PostProduct postProduct) {
        postProduct.setId(++sequence);
        return postProduct;
    }

    public PostProduct save(PostProduct postProduct) {
        return postProduct;
    }


    public void clearStore() {
        store.clear();
    }

}
