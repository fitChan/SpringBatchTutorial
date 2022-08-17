package com.example.springbatchtutorial.job.FileDataReadWriteConfig.entity;

import lombok.Data;

import java.time.Year;

@Data
public class Player {
    private String ID;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;


}
