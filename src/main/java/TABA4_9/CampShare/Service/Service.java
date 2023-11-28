package TABA4_9.CampShare.Service;

import TABA4_9.CampShare.Entity.Entity;
import TABA4_9.CampShare.Repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class Service {

    private Repository repository;

    public Entity write(Entity entity){
        return repository.save(entity);
    }
    public Entity find(String content){
        return repository.findByContent(content);
    }

    public void remove(Entity entity){
        repository.delete(entity);
    }
}
