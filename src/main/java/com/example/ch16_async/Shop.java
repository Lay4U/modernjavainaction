package com.example.ch16_async;

import com.example.modernjavainaction.chap16.Discount;
import com.example.modernjavainaction.chap16.Quote;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static java.lang.String.format;
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

    List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"));

    public class 비블록코드 {


//        public List<String> findPrices(String product) {
//            return shops.stream()
//                    .map(shop -> format("%s price is %.2f",
//                            shop.getName(), shop.getPrice(product)))
//                    .toList();
//        }

        public List<String> findPricesAsync(String product) {
            return shops.parallelStream()
                    .map(shop -> format("%s price is %.2f",
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

        public static class Discount {
            public enum Code {
                NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

                private final int percentage;

                Code(int percentage) {
                    this.percentage = percentage;
                }
            }
        }

        public String getPrice(String product) {
            Random random = new Random();
            double price = calculatePrice(product);
            Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
            return format("%s:%.2f:%s", name, price, code);
        }

        public double calculatePrice(String product) {
            Random random = new Random();
            delay();
            return random.nextDouble() * product.charAt(0) + product.charAt(1);
        }

//        public static String applyDiscount(Quote quote) {
//            return quote.getShopName() + " price is " +
//                    apply(quote.getPrice(), quote.getDiscountCode());
//        }

        public static double apply(double price, Discount.Code code) {
            delay();
            return price * (100 - code.percentage) / 100;
        }

        //    public List<String> findPrices(String product){
//        return shops.stream()
//                .map(shop -> shop.getPrice(product))
//                .map(Quote::parse)
//                .map(비블록코드::applyDiscount)
//                .toList();
//    }


//        public List<String> findPrices(String product) {
//            List<CompletableFuture<String>> priceFutures = shops.stream()
//                    .map(shop -> CompletableFuture.supplyAsync(() ->
//                            shop.getPrice(product), executor))
//                    .map(future -> future.thenApply(Quote::parse))
//                    .map(future -> future.thenCompose(quote ->
//                            CompletableFuture.supplyAsync(() ->
//                                    Discount.applyDiscount(quote), executor)));
//
//            return priceFutures.stream()
//                    .map(CompletableFuture::join)
//                    .toList();
//        }
//    }
//
//    public static class Quote {
//        private final String shopName;
//        private final double price;
//        private final 비블록코드.Discount.Code discountCode;
//
//        public Quote(String shopName, double price, 비블록코드.Discount.Code discountCode) {
//            this.shopName = shopName;
//            this.price = price;
//            this.discountCode = discountCode;
//        }
//
//        public static Quote parse(String s) {
//            String[] split = s.split(":");
//            String shopName = split[0];
//            double price = Double.parseDouble(split[1]);
//            비블록코드.Discount.Code discountCode = 비블록코드.Discount.Code.valueOf(split[2]);
//            return new Quote(shopName, price, discountCode);
//        }
//
//        public String getShopName() {
//            return shopName;
//        }
//
//        public double getPrice() {
//            return price;
//        }
//
//        public 비블록코드.Discount.Code getDiscountCode() {
//            return discountCode;
//        }
//    }
//
//    public void stuff() {
//        Shop shop = new Shop("");
//        String product = "";
//        Future<Double> futurePriceInUSE =
//                CompletableFuture.supplyAsync(() -> shop.getPrice(product))
//                        .thenCombine(CompletableFuture.supplyAsync(
//                                        () -> exchangeService.getRate(Money.EUR, Money.USD)),
//                                (price, rate) -> price * rate)
//                ));
//
//        ExecutorService executor = Executors.newCachedThreadPool();
//        executor.submit(new Callable<Double>(){
//            public Double call(){
//                return exchangeService.getRage(Money, EUR, Money.USD);
//            }
//        });
//        executor.submit(new Callable<Double>(){
//            public Double call(){
//                double priceInEUR = shop.getPrice(product);
//                return priceInEUR * futureRage.get();
//            }
//        });
//    }
//
//    private static final Random random = new Random();
//    public void CompletableFuture종료() {
//        int delay = 500 + random.nextInt(2000);
//        try{
//            Thread.sleep(delay);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public Stream<CompletableFuture<String>> findPriceStream(String product){
//        return shops.stream()
//                .map(shop -> CompletableFuture.supplyAsync(
//                        () -> shop.getPrice(product), executor))
//                .map(future -> future.thenApply(Quote::parse))
//                .map(future -> future.thenCompose(quote ->
//                        CompletableFuture.supplyAsync(
//                                () -> Discount.applyDiscount(quote), executor)));

    }



}
