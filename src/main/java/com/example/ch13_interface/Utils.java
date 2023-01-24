package com.example.ch13_interface;

import java.util.List;

public class Utils {
    public static void paint(List<Resizable> l){
        l.forEach(r -> {
            r.setAbsoluteSize(42, 42);
            r.draw();
        })
    }
}
