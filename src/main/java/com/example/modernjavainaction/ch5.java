package com.example.modernjavainaction;

import java.util.List;

import static com.example.modernjavainaction.ch4.menu;

// 스트림 활용
public class ch5 {
    public void filtering(){
        List<Dish> vegetarianMenu = menu.stream()
                .filter(Dish::isVegetarian)
                .toList();

        List<Integer> numbers = List.of(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);
        // 고유의 여부는 객체의 hashCode, equals로 결정됨


    }
}
