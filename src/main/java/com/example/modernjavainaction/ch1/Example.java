//package com.example.modernjavainaction.ch1;
//
//import java.io.File;
//import java.io.FileFilter;
//import java.util.*;
//import java.util.function.Predicate;
//
//public class Example {
//    public static void main(String[] args) {
//        Collections.sort(inventory, new Comparator<Apple>() {
//            public int compare(Apple a1, Apple a2) {
//                return a1.getWeight().compareTo(a2.getWeight());
//            }
//        });
//
//        inventory.sort(comparing(Apple::getWeight));
//
//        File[] hiddenFiles = new File(".").listFiles(new FileFilter() {
//            public boolean accept(File file) {
//                return file.isHidden();
//            }
//        });
//
//        File[] hiddenFiles2 = new File(".").listFiles(File::isHidden);
//
//    }
//
//    public static List<Apple> filterGreenApples(List<Apple> inventory) {
//        List<Apple> result = new ArrayList<>();
//
//        for (Apple apple : inventory) {
//            if (GREEN.equals(apple.getColor())){
//                result.add(apple);
//            }
//        }
//        return result;
//    }
//    public static List<Apple> filterHeavyApples(List<Apple> inventory) {
//        List<Apple> result = new ArrayList();
//
//        for (Apple apple : inventory) {
//            if (aplle.getWeight() > 150){
//                result.add(apple);
//            }
//        }
//        return result;
//    }
//
//    public static boolean isGreenApple(Apple apple){
//        return GREEN.equals(apple.getColor());
//    }
//
//    public static boolean isHeavyApple(Apple apple){
//        return apple.getWeight() > 150;
//    }
//
//    public interface List<Apple> Predicate<T>{
//        boolean test(T t);
//    }
//
//    static List<Apple> filterApples(List<Apple> inventory,
//                                    Predicate<Apple> p){
//        List<Apple> result = new ArrayList<>();
//        for (Apple apple: inventory){
//            if (p.test(apple)){
//                result.add(apple);
//            }
//        }
//        return result;
//    }
//
//    filterApples(inventory, APple::isGreenApple);
//    filterApples(inventory, APple::isHeavyApple);
//
//    filterApples(inventory, (Appla a) -> a.getWeight() < 80 ||
//            GREEN.eqauls(a.getColor()));
//    public void transaction(){
//        Map<Currency, List<Transaction>> transactionByCurrencies = new HashMap<>();
//        for (Transaction transaction: transactions){
//            if (transaction.getPrice() > 1000){
//                Currency currency = transaction.getCurrency();
//                List<Transcation> transactionsForCurrency = transactionsByCurrencies.get(currency);
//                if (transactionsForCurrency == null){
//                    transactionsForCurrency = new ArrayList<>();
//                    transactionsByCurrencies.put(currency, transactionsForCurrency);
//                }
//                transactionsForCurrency.add(transaction);
//            }
//        }
//    }
//
//    public void transactionStream(){
//        Map<Currency, List<Transaction>> transactionByCurrencies = transactions.stream()
//                .filter((Transaction t) -> t.getPrice() > 1000)
//                .collect(groupingBy(Transaction::getCurrency));
//    }
//
//    List<Apple> heavyApples = inventory.stream().filter((Apple a) -> a.getWeight() > 150)
//            .collect(toList());
//
//    List<Apple> heavyApples = inventory.parallelStream().filter((Apple a) -> a.getWeight() > 150)
//            .collect(toList());
//
//
//
//}
