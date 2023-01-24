package com.example.ch13_interface;

public class Monster implements Rotatable, Moveable, Resizable{
    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void setWidth(int width) {

    }

    @Override
    public void setHeight(int height) {

    }

    @Override
    public void setAbsoluteSize(int width, int height) {

    }

    @Override
    public void setRotationAngle(int angleInDegrees) {

    }

    @Override
    public int getRotationAngle() {
        return 0;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void setX(int x) {

    }

    @Override
    public void setY(int y) {

    }

    public static void main(String[] args) {
        Monster monster = new Monster();
        monster.rotateBy(100);
        monster.moveVertically(10);
    }
}
