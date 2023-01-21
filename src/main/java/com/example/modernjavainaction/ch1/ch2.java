package com.example.modernjavainaction.ch1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

import static com.example.modernjavainaction.ch1.ch2.Color.GREEN;
import static com.example.modernjavainaction.ch1.ch2.Color.RED;

public class ch2 {
    public enum Color { RED, GREEN }

    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (GREEN.toString().equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByColor(final List<Apple> inventory, final Color color){
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ( apple.getColor().equals(color.toString()) ) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight){
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ( apple.getWeight() > weight ) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApples2(final List<Apple> inventory,
                                           final Color color,
                                           final int weight,
                                           final boolean flag){
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ((flag && apple.getColor().equals(color.toString()) ||
                    (!flag && apple.getWeight() > weight))) {
                result.add(apple);
            }
        }
        return result;
    }

    //flag에 true, false가 의미하는게 무엇일까?, 요구사항이 또 바뀌면?

    //동작을 파라미터화

    public interface ApplePredicate {
        boolean test (Apple apple);
    }

//    public class AppleHeavyWeightPredicate implements ApplePredicate {
//
//        @Override
//        public boolean test(Apple apple) {
//            return apple.getWeight() > 150;
//        }
//    }
//
//    public class AppleGreenColorPredicate implements ApplePredicate {
//
//        @Override
//        public boolean test(Apple apple) {
//            return GREEN.equals(apple.getColor());
//        }
//    }
//
//    public static List<Apple> filterApples(List<Apple> inventory,
//                                           ApplePredicate p){
//        List<Apple> result = new ArrayList<>();
//        for (Apple apple : inventory) {
//            if(p.test(apple)){
//                result.add(apple);
//            }
//        }
//        return result;
//    }

    public class AppleRedAndHeavyPredicate implements ApplePredicate{

        @Override
        public boolean test(Apple apple) {
            return RED.toString().equals(apple.getColor())
                    && apple.getWeight() > 150;
        }
    }



    public interface AppleFormatter {
        String accept(Apple a);
    }

    public class AppleFancyFormatter implements AppleFormatter{

        @Override
        public String accept(Apple apple) {
            String characteristic = apple.getWeight() > 105 ? "heavy" : "light";
            return "A " + characteristic + " " + apple.getColor() + " apple";
        }
    }

    public class AppleSimpleFormatter implements AppleFormatter{
        @Override
        public String accept(Apple apple) {
            return "An apple of " + apple.getWeight() + "g";
        }
    }

    public static void prettyPrintApple(List<Apple> inventory,
                                        AppleFormatter formatter){
        for (Apple apple : inventory) {
            String output = formatter.accept(apple);
            System.out.println(output);
        }
    }


    public static class AppleHeavyWeightPredicate implements ApplePredicate {

        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() > 150;
        }
    }

    public class AppleGreenColorPredicate implements ApplePredicate {

        @Override
        public boolean test(Apple apple) {
            return GREEN.toString().equals(apple.getColor());
        }
    }


    public class FilteringApples {
        public void main(String[] args) {
            List<Apple> inventory = Arrays.asList(new Apple(80, GREEN),
                                                    new Apple(155, GREEN),
                                                    new Apple(120, RED));


            List<Apple> heavyApples = filterApples(inventory, new AppleHeavyWeightPredicate());
            List<Apple> greenApples = filterApples(inventory, new AppleGreenColorPredicate());

            List<Apple> redApples = filterApples(inventory, new ApplePredicate(){
                @Override
                public boolean test(Apple apple){
                    return RED.toString().equals(apple.getColor());
                }
            });

            List<Apple> result = filterApples(inventory, (Apple apple) -> RED.toString().equals(apple.getColor()));

            List<Apple> redApples2
                    = filter(inventory, (Apple apple) -> RED.toString().equals(apple.getColor()));

            List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
            List<Integer> evenNumbers
                    = filter(numbers, (Integer i) -> i % 2 == 0);


        }


    }
    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T e : list) {
            if (p.test(e)) {
                result.add(e);
            }
        }
        return result;
    }

    public static List<Apple> filterApples(List<Apple> inventory,
                                           ApplePredicate p){
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }









//    public interface Comparator<T>{
//        int compare(T o1, T o2);
//    }
//
//
//    public interface Runnable {
//        void run();
//    }

    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(new Apple(80, GREEN),
                new Apple(155, GREEN),
                new Apple(120, RED));

        inventory.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple a1, Apple a2) {
                return a1.getWeight().compareTo(a2.getWeight());
            }
        });

        inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        });

        Thread t2 = new Thread(() -> System.out.println("Hello"));


        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> threadName = executorService.submit(new Callable<String>() {
            @Override
            public String call() {
                return Thread.currentThread().getName();
            }
        });

        Future<String> threadName2 = executorService.submit(() -> Thread.currentThread().getName());


//        Button button = new Button("Send");
//        button.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent event) {
//                label.setText("Sent!!");
//            }
//        });
//
//        button.setOnAction((event) -> label.setText("Sent!!"));
    }




}
