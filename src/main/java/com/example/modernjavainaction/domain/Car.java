package com.example.modernjavainaction.domain;

public class Car {
    private String color;
    private String brand;

    public Car(String color, String brand) {
        this.color = color;
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public String getBrand() {
        return brand;
    }
}
