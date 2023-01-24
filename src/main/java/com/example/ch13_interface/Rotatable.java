package com.example.ch13_interface;

public interface Rotatable {
    void setRotationAngle(int angleInDegrees);
    int getRotationAngle();
    default void rotateBy(int angleInDegrees){
        setRotationAngle((getRotationAngle() + angleInDegrees) % 360);
    }
}
