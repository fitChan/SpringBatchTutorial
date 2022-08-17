package com.example.springbatchtutorial.job.HelloWorld.tistory2.Entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Year;
import java.util.Date;

@Entity
@Getter @NoArgsConstructor
public class ChefCareer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String mainMenu;
    private int carrer;

    public ChefCareer(Chef chef) {
        this.id = chef.getId();
        this.name = chef.getName();
        this.mainMenu = chef.getMainMenu();
        this.carrer = Year.now().getValue()-chef.getDebut();
    }
}
