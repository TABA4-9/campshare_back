package TABA4_9.CampShare.Repository;

import TABA4_9.CampShare.Entity.PostProduct;
import java.util.*;


public class PostProductRepository {
    private Map<Long, PostProduct> store = new HashMap<>();

    private static Long sequence = 0L;

    private static final PostProductRepository instance = new PostProductRepository();

    public static PostProductRepository getInstance() {
        return instance;
    }

    private PostProductRepository() {

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
