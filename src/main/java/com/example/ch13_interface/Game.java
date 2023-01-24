package com.example.ch13_interface;

import java.util.Arrays;
import java.util.List;

public class Game {
    public static void main(String[] args) {
        List<Object> resizableShapes = Arrays.asList(new Square(), new Reactangle(), new Ellipse());
        Utils.paint(resizableShapes);
    }
}
