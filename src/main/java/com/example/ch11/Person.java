package com.example.ch11;


import java.util.Optional;

public class Person {
    private Integer age;
    private Optional<Car> car;
    public Optional<Car> getCar() {
        return car;
    }

    public Integer getAge() {
        return age;
    }

}
