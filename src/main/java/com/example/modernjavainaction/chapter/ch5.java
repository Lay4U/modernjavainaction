package com.example.modernjavainaction.chapter;

import com.example.modernjavainaction.domain.Dish;
import com.example.modernjavainaction.domain.Trader;
import com.example.modernjavainaction.domain.Transaction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.modernjavainaction.chapter.ch4.menu;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

// 스트림 활용
public class ch5 {
    public void filtering() {
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

    public void 스트림슬라이싱() {
        List<Dish> specialMenu = Arrays.asList(
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER));

        List<Dish> filteredMenu = specialMenu.stream()
                .filter(dish -> dish.getCalories() < 320)
                .toList();

        List<Dish> slicedMenu = specialMenu.stream()
                .takeWhile(dish -> dish.getCalories() < 320)
                .toList();
        List<Dish> slicedMenu2 = specialMenu.stream()
                .dropWhile(dish -> dish.getCalories() < 320)
                .toList();


        List<Dish> dishes = specialMenu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .limit(3)
                .toList();


        List<Dish> dishes2 = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .skip(2)
                .toList();
    }

    public void 매핑() {
        List<Integer> dishNameLengths = menu.stream()
                .map(Dish::getName)
                .map(String::length).toList();


        List<String> words = Arrays.asList("Modern", "Java", "In", "Action");
        List<String[]> floatMap = words.stream()
                .map(word -> word.split(""))
                .distinct()
                .toList();

        String[] arrayOfWords = {"Goodbye", "World"};
        Stream<String> streamOfWords = Arrays.stream(arrayOfWords);

        words.stream()
                .map(word -> word.split(""))
                .map(Arrays::stream)
                .distinct()
                .toList();

        List<String> uniqueCharacters = words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .toList();


        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> squares = numbers.stream()
                .map(n -> n * n)
                .toList();


        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> pairs = numbers1.stream()
                .flatMap(i -> numbers2.stream()
                        .map(j -> new int[]{i, j}))
                .toList();

        List<int[]> pairsFilter = numbers1.stream()
                .flatMap(i ->
                        numbers2.stream()
                                .filter(j -> (i + j) % 3 == 0)
                                .map(j -> new int[]{i, j}))
                .toList();
    }

    public void 검색과매칭() {
        if (menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("The menu is (somewhat) vegetarian friendly!!");
        }

        boolean isHealthy = menu.stream().allMatch(dish -> dish.getCalories() < 1000);

        boolean isHealth2 = menu.stream().noneMatch(dish -> dish.getCalories() >= 1000);

        Optional<Dish> dish = menu.stream()
                .filter(Dish::isVegetarian)
                .findAny();
        dish
                .ifPresent(dishElement -> System.out.println(dishElement.getName()));

        List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);

        Optional<Integer> firstSquareDivisibleByTree = someNumbers.stream()
                .map(n -> n * n)
                .filter(n -> n % 3 == 0)
                .findFirst();
    }

    public void 리듀싱() {
        List<Integer> numbers = Arrays.asList(3, 4, 5, 1, 2);
        int sum = 0;
        for (int x : numbers) {
            sum += x;
        }

        Integer sum2 = numbers.stream().reduce(0, (a, b) -> a + b);
        Integer sum3 = numbers.stream().reduce(0, Integer::sum);
        Integer product = numbers.stream().reduce(1, (a, b) -> a * b);
        Optional<Integer> sum4 = numbers.stream().reduce((a, b) -> a + b); // 스트림이 빌경우 null 반환

        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        Optional<Integer> min = numbers.stream().reduce(Integer::min);


        Integer count = menu.stream()
                .map(d -> 1)
                .reduce(0, (a, b) -> a + b);

        long count2 = menu.stream().count();
        long count3 = menu.size();
    }

    Trader raoul = new Trader("Raoul", "Cambridge");
    Trader mario = new Trader("Mario", "Milan");
    Trader alan = new Trader("Alan", "Cambridge");
    Trader brian = new Trader("Brian", "Cambridge");

    List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
    );

    public void practice() {
        List<Transaction> transaction2011 = transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(comparing(Transaction::getValue))
                .toList();

        List<String> city = transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .distinct()
                .toList();
        Set<String> citySet = transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .collect(Collectors.toSet());


        List<Trader> traders = transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .distinct()
                .sorted(comparing(Trader::getName))
                .toList();

        String traderName = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("", (n1, n2) -> n1 + n2);

        transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .collect(joining());

        boolean milan = transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));

        transactions.stream()
                .filter(t -> "Cambridge".equals(t.getTrader().getCity()))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        Optional<Integer> max = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);

        Optional<Transaction> min = transactions.stream()
                .reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2);
        Optional<Transaction> min2 = transactions.stream()
                .min(comparing(Transaction::getValue));
    }

    public void 숫자형스트림() {
        Integer calories = menu.stream()
                .map(Dish::getCalories)
                .reduce(0, Integer::sum);

        int c1 = menu.stream()
                .mapToInt(Dish::getCalories)
                .sum();

        IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
        Stream<Integer> stream = intStream.boxed();

        OptionalInt max = menu.stream()
                .mapToInt(Dish::getCalories)
                .max();
        int max2 = max.orElse(1);

        IntStream evenNumbers = IntStream.rangeClosed(1, 100)
                .filter(n -> n % 2 == 0);
        System.out.println(evenNumbers.count());
    }

    public static void main(String[] args) {
        Stream<double[]> pythagoreanTriples =
                IntStream.rangeClosed(1, 100).boxed()
                        .flatMap(a ->
                                IntStream.rangeClosed(a, 100)
                                        .mapToObj(b ->
                                                new double[]{a, b, Math.sqrt(a * a + b * b)})
                                        .filter(t -> t[2] % 1 == 0));

        pythagoreanTriples.limit(5)
                .forEach(t ->
                        System.out.println(t[0] + ", " + t[1] + ", " + t[2])
                );
    }

    public void 스트림만들기(){

        Stream<String> stream = Stream.of("Modern", "Java", "In", "Action");
        stream.map(String::toUpperCase)
                .forEach(System.out::println);

        Stream<Object> emptyStream = Stream.empty();

        String homeValue = System.getProperty("home");
        Stream<String> homeValueStream
                = homeValue == null ? Stream.empty() : Stream.of(homeValue);

        Stream<String> homeValueStream2
                = Stream.ofNullable(System.getProperty("home"));

        Stream<String> values = Stream.of("config", "home", "user")
                .flatMap(key -> Stream.ofNullable(System.getProperty(key)));

        int[] numbers = {2, 3, 5, 7, 11, 13};
        int sum = Arrays.stream(numbers).sum();

        long uniqueWords = 0;
        try(Stream<String> lines = Files.lines(Paths.get("data.txt"),
                Charset.defaultCharset())){
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        }catch (IOException e){
            e.printStackTrace();
        }


        Stream.iterate(0, n-> n+2)
                .limit(10)
                .forEach(System.out::println);


        Stream.iterate(new int[]{0, 1},
                t -> new int[]{t[1], t[0] + t[1]})
                .limit(20)
                .forEach(t -> System.out.println("(" + t[0] + ", " + t[1] + ")"));

        IntStream.iterate(0, n -> n < 100, n -> n + 4)
                .forEach(System.out::println);

        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

        IntStream.generate(new IntSupplier(){
            @Override
            public int getAsInt(){
                return 2;
            }
        });

        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1;
            @Override
            public int getAsInt() {
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };

        IntStream.generate(fib).limit(10).forEach(System.out::println);
    }


}
