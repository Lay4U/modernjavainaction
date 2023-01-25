package com.example.ch16_async;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class Shop {
    private String name;

    public Shop(String bestShop) {
        new Shop(bestShop);
    }

    public String getName() {
        return name;
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public Future<Double> getPriceAsync(String product) {
//        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
//        new Thread( () -> {
//            double price = calculatePrice(product);
//            futurePrice.complete(price);
//        }).start();
//
//        return futurePrice;
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
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
        try {
            Double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e) {
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

    public Future<Double> getPriceAsync2(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception ex) {
                futurePrice.completeExceptionally(ex);
            }
        }).start();
        return futurePrice;
    }

    public class 비블록코드 {
        List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
                new Shop("LetsSaveBig"),
                new Shop("MyFavoriteShop"),
                new Shop("BuyItAll"));

        public List<String> findPrices(String product) {
            return shops.stream()
                    .map(shop -> String.format("%s price is %.2f",
                            shop.getName(), shop.getPrice(product)))
                    .toList();
        }

        public List<String> findPricesAsync(String product) {
            return shops.parallelStream()
                    .map(shop -> String.format("%s price is %.2f",
                            shop.getName(), shop.getPrice(product)))
                    .toList();
        }

        public List<String> findPricesAsync2(String product) {
            List<CompletableFuture<String>> priceFutures = shops.stream()
                    .map(shop -> CompletableFuture.supplyAsync(() ->
                            shop.getName() + " price is " + shop.getPrice(product)))
                    .toList();

            return priceFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        }

        // Custom Excutor

        private final Executor executor =
                Executors.newFixedThreadPool(Math.min(shops.size(), 100),
                        new ThreadFactory() {
                            public Thread newThread(Runnable r) {
                                Thread t = new Thread(r);
                                t.setDaemon(true);
                                return t;
                            }
                        });

        public List<String> findPricesAsync3(String product) {
            List<CompletableFuture<String>> priceFutures = shops.stream()
                    .map(shop -> CompletableFuture.supplyAsync(() ->
                            shop.getName() + " price is " + shop.getPrice(product), executor))
                    .toList();

            return priceFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        }

        public class Discount {
            public enum Code{
                NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

                private final int percentage;

                Code(int percentage){
                    this.percentage = percentage;
                }
            }
        }



    }
}
