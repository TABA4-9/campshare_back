package TABA4_9.CampShare.Entity;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name="tibero_test")  //table명
public class Entity {
    @Id
    @Column(name="id", nullable = false)
    private int id;
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="content", nullable = false)
    private String content;
    public Entity(){
        this.name="anonymous";
        this.content="None";
    }
    public String getName(){
        return name;
    }
    public String getContent(){
        return content;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setContent(String content){
        this.content=content;
    }


}
