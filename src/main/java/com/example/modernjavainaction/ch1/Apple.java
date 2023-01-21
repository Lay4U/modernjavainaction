package com.example.modernjavainaction.ch1;

import com.example.modernjavainaction.ch1.ch2.Color;

public class Apple {
    private String color;
    private Integer weight;

    public Apple(int weight, Color color) {
        this.weight = weight;
        this.color = color.toString();
    }

    public Apple() {

    }

    public Apple(Integer weight) {
        this.weight = weight;
    }

    public Apple(Color color, Integer integer) {
        this.color = color.toString();
        this.weight = integer;
    }

    public String getColor() {
        return color;
    }

    public Integer getWeight() {
        return weight;
    }

    public Apple setColor(String color) {
        this.color = color;
        return this;
    }

    public Apple setWeight(int weight) {
        this.weight = weight;
        return this;
    }

}
