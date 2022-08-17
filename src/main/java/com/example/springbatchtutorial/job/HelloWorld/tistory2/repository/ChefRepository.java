package com.example.springbatchtutorial.job.HelloWorld.tistory2.repository;

import com.example.springbatchtutorial.job.HelloWorld.tistory2.Entity.Chef;
import com.example.springbatchtutorial.job.HelloWorld.tistory2.Entity.ChefCareer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Integer> {
}
