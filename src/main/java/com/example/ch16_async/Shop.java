package com.example.ch16_async;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;

public class Shop {
    private String name;
    public Shop(String bestShop) {
        new Shop(bestShop);
    }

    public double getPrice(String product){
        return calculatePrice(product);
    }

    public Future<Double> getPriceAsync(String product){
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread( () -> {
            double price = calculatePrice(product);
            futurePrice.complete(price);
        }).start();

        return futurePrice;
    }
    private double calculatePrice(String product) {
        delay();
        Random random = new Random();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public static void delay() {
        try {
            sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long invocationTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Invocation returned after " + invocationTime + " msecs");
        doSomethingElse();
        try{
            Double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        long retrievalTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Price returned after " + retrievalTime + " msecs");
    }

    private static void doSomethingElse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
