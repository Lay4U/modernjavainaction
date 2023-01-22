package com.example.modernjavainaction.chapter;

import com.example.modernjavainaction.domain.Dish;
import com.example.modernjavainaction.custom.PrimeNumbersCollector;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.modernjavainaction.chapter.ch4.menu;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

public class ch6 {
    public void 스트림으로데이터수집() {
//        Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap<>();
//        for (Transaction transaction : transactions) {
//            Currency currency = transaction.getCurrency();
//            List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);
//            if (transactionsForCurrency == null) {
//                transactionsForCurrency = new ArrayList<>();
//                transactionsByCurrencies.put(currency, transactionsForCurrency);
//            }
//            transactionsForCurrency.add(transaction);
//
//            Map<Currency, List<Transaction>> transactionsByCurrencies2 =
//                    transactions.stream().collect(groupingBy(Transaction::getCurrency));

    }

    public void 리듀싱과요약() {
        menu.stream()
                .collect(Collectors.counting());
        menu.stream()
                .count();
        menu.size();

        Comparator<Dish> dishCaloriesComparator = Comparator.comparing(Dish::getCalories);

        Optional<Dish> mostCalorieDish = menu.stream()
                .collect(maxBy(dishCaloriesComparator));

        Integer totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
        Integer totalCalories2 = menu.stream().mapToInt(Dish::getCalories).sum();

        Double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));

        IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
        menuStatistics.getAverage();
        menuStatistics.getCount();
        menuStatistics.getMax();
        menuStatistics.getMin();
        menuStatistics.getSum();

        String shortMenu = menu.stream().map(Dish::getName).collect(joining());

        menu.stream().map(Dish::getName).collect(joining(", "));


        Integer totalCalories3 = menu.stream().collect(reducing(
                0, Dish::getCalories, (i, j) -> i + j));
        Integer totalCalories4 = menu.stream()
                .map(Dish::getCalories).reduce(0, (i, j) -> i + j);
        Integer totalCalories5 = menu.stream()
                .map(Dish::getCalories).reduce(0, Integer::sum);

        Optional<Dish> mostCalorieDish2 = menu.stream().collect(reducing(
                (d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
        Optional<Dish> mostCalorieDish3 = menu.stream()
                .reduce((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2);

        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5).stream();
        stream.reduce(
                new ArrayList<Integer>(),
                (List<Integer> l, Integer e) -> {
                    l.add(e);
                    return l;
                },
                (List<Integer> l1, List<Integer> l2) -> {
                    l1.addAll(l2);
                    return l1;
                }
        );
        // 위 reduce는 여러 스레드가 동시에 같은 데이터 구조체를 고치면 리스트 자체가 망가져 버려 리듀싱을 병렬로 수행할 수 없다.
        // 이걸 해결하려면 매번 새로운 리스트를 할당해야하고, 객체를 할다아느라 성능이 저하된다.
        // 가변 컨테이너 관련 작업이면서 병렬성을 확보하려면 collect 메서드로 리듀싱 연산을 구현하는게 바람직하다.

        Integer totalCalories6 = menu.stream().collect(reducing(0,
                Dish::getCalories,
                Integer::sum));
        Integer totalCalories7 = menu.stream().map(Dish::getCalories)
                .reduce(0, Integer::sum);

        Integer totalCalories8 = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();

        int totalCalories9 = menu.stream().mapToInt(Dish::getCalories).sum();


    }

    public static <T> Collector<T, ?, Long> counting() {
        return reducing(0L, e -> 1L, Long::sum);
    }

    public enum CaloricLevel {DIET, NORMAL, FAT}

    public void 그룹화() {
        Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));


        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(
                groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                })
        );

        Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream().filter(dish -> dish.getCalories() > 500)
                .collect(groupingBy(Dish::getType));

        Map<Dish.Type, List<Dish>> caloricDishesByType2 = menu.stream()
                .collect(groupingBy(Dish::getType,
                        filtering(dish -> dish.getCalories() > 500, toList())));

        Map<Dish.Type, List<String>> dishNamesByType = menu.stream()
                .collect(groupingBy(Dish::getType,
                        mapping(Dish::getName, toList())));

        Map<String, List<String>> dishTags = new HashMap<>();
        dishTags.put("pork", Arrays.asList("greasy", "salty"));
        dishTags.put("beef", Arrays.asList("salty", "roasted"));
        dishTags.put("chicken", Arrays.asList("fried", "crisp"));
        dishTags.put("french fries", Arrays.asList("greasy", "fried"));
        dishTags.put("rice", Arrays.asList("light", "natural"));
        dishTags.put("season fruit", Arrays.asList("fresh", "natural"));
        dishTags.put("pizza", Arrays.asList("tasty", "salty"));
        dishTags.put("prawns", Arrays.asList("tasty", "roasted"));
        dishTags.put("salmon", Arrays.asList("delicious", "fresh"));

        Map<Dish.Type, Set<String>> dishNamesByType2 = menu.stream()
                .collect(groupingBy(Dish::getType,
                        flatMapping(dish -> dishTags.get(dish.getName()).stream(), toSet())));

        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel =
                menu.stream().collect(
                        groupingBy(Dish::getType,
                                groupingBy(dish -> {
                                    if (dish.getCalories() <= 400)
                                        return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700)
                                        return CaloricLevel.NORMAL;
                                    else
                                        return CaloricLevel.FAT;
                                }))
                );

        Map<Dish.Type, Long> typesCount = menu.stream().collect(
                groupingBy(Dish::getType, counting())
        );

        Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream()
                .collect(groupingBy(Dish::getType,
                        maxBy(comparingInt(Dish::getCalories))));

        Map<Dish.Type, Dish> mostCaloricByType2 = menu.stream()
                .collect(groupingBy(Dish::getType,
                        collectingAndThen(
                                maxBy(comparingInt(Dish::getCalories)),
                                Optional::get
                        )));

        Map<Dish.Type, Dish> mostCaloricByType3 = menu.stream()
                .collect(toMap(Dish::getType,
                        Function.identity(),
                        BinaryOperator.maxBy(comparingInt(Dish::getCalories))));

        Map<Dish.Type, Integer> totalCaloriesByType = menu.stream()
                .collect(groupingBy(
                        Dish::getType,
                        summingInt(Dish::getCalories)
                ));

        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream().collect(
                groupingBy(Dish::getType, mapping(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                }, toSet()))
        );
        Map<Dish.Type, HashSet<CaloricLevel>> caloricLevelsByTypeHashSet =
                menu.stream().collect(
                        groupingBy(Dish::getType, mapping(dish -> {
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        }, toCollection(HashSet::new))));
    }

    public void 분할() {
        Map<Boolean, List<Dish>> partitionedMenu =
                menu.stream().collect(partitioningBy(Dish::isVegetarian));

        List<Dish> vegetarianDishes = partitionedMenu.get(true);

        List<Dish> vegetarianDishes2 =
                menu.stream().filter(Dish::isVegetarian).toList();

        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType = menu.stream()
                .collect(
                        partitioningBy(Dish::isVegetarian,
                                groupingBy(Dish::getType))
                );

        Map<Boolean, Dish> mostCaloricPartitionedByVegetarian =
                menu.stream().collect(
                        partitioningBy(Dish::isVegetarian,
                                collectingAndThen(
                                        maxBy(comparingInt(Dish::getCalories)),
                                        Optional::get)));

    }

    public boolean isPrimeOld(int candidate) {
        return IntStream.range(2, candidate)
                .noneMatch(i -> candidate % i == 0);
    }

    public static boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
        return IntStream.rangeClosed(2, n).boxed()
                .collect(partitioningBy(candidate -> isPrime(candidate)));
    }


    public void CollectorInterface() {

    }

    public interface myCollector<T, A, R> {
        Supplier<A> supplier();

        BiConsumer<A, T> accumulator();

        Function<A, R> finisher();

        BinaryOperator<A> combiner();

        Set<Collector.Characteristics> characteristics();
    }

    //    public Supplier<List<T>> supplier() {
//        return () -> new ArrayList<>();
//    }
//    public BiConsumer<List<T>, T> accumulator() {
//        return List::add;
//    }
//    public Function<List<T>, List<T>> finisher(){
//        return Function.identity();
//    }
//    public BinaryOperator<List<T>> combiner(){
//        return (list1, list2) -> {
//            list1.addAll(list2);
//            return list1;
//        }
//    }
    public void 커스텀컬렉터를구현성능개선() {
        ArrayList<Dish> dishes = menu.stream().collect(
                ArrayList::new, //발생
                List::add, //누적
                List::addAll //합침
        );
    }

    public boolean isPrimeAdvanced(List<Integer> primes, int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return primes.stream()
                .takeWhile(i -> i <= candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    //자바 8로 takeWhile 흉내내기
    public static <A> List<A> myTakeWhile(List<A> list, Predicate<A> p) {
        int i = 0;
        for (A item : list) {
            if (!p.test(item)) {
                return list.subList(0, i);
            }
            i++;
        }
        return list;
    }

    public static boolean myIsPrime(List<Integer> primes, int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return myTakeWhile(primes, i -> i <= candidateRoot)
                .stream()
                .noneMatch(p -> candidate % p == 0);
    }

    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
        return IntStream.rangeClosed(2, n).boxed()
                .collect(new PrimeNumbersCollector());
    }

    public static HashMap<Boolean, List<Integer>> partitionPrimesWithCustomCollectionImplement(int n){
        return IntStream.rangeClosed(2, n).boxed()
                .collect(
                        () -> new HashMap<Boolean, List<Integer>>() {{
                            put(true, new ArrayList<>());
                            put(false, new ArrayList<>());
                        }},
                        (acc, candidate) -> {
                            acc.get(isPrime(acc.get(true), candidate))
                                    .add(candidate);
                        },
                        (map1, map2) -> {
                            map1.get(true).addAll(map2.get(true));
                            map1.get(false).addAll(map2.get(false));
                        });
    }

    public class CollectorHarness {
        public static void main(String[] args) {
            long fastest = Long.MAX_VALUE;
            for (int i = 0; i < 10; i++) {
                long start = System.nanoTime();
                partitionPrimes(1_000_000);
                long duration = (System.nanoTime() - start) / 1_000_000;
                if (duration < fastest) fastest = duration;
            }
            System.out.format("Fastest execution done in %d msecs", fastest);
        }
    }

    public static void main(String[] args) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
//            partitionPrimes(1_000_000); // Fastest execution done in 358 msecs
            partitionPrimesWithCustomCollector(1_000_000); //Fastest execution done in 127 msecs
            long duration = (System.nanoTime() - start) / 1_000_000;
            if (duration < fastest) fastest = duration;
        }
        System.out.format("Fastest execution done in %d msecs", fastest);
    }

    private static boolean isPrime(List<Integer> primes, int candidate){
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return primes.stream()
                .takeWhile(i -> i <= candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }
}
