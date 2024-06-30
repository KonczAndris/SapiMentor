package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer status = 1;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }


}
