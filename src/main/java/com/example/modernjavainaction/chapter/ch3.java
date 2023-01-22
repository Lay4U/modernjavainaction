package com.example.modernjavainaction.chapter;

import com.example.modernjavainaction.domain.Apple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

import static com.example.modernjavainaction.chapter.ch2.Color;
import static com.example.modernjavainaction.chapter.ch2.Color.GREEN;
import static com.example.modernjavainaction.chapter.ch2.Color.RED;
import static java.util.Comparator.comparing;

public class ch3 {
    public static void main(String[] args) {
        Comparator<Apple> byWeight = new Comparator<Apple>() {
            @Override
            public int compare(Apple a1, Apple a2) {
                return a1.getWeight().compareTo(a2.getWeight());
            }
        };

        Comparator<Apple> byWeight2 = (Apple a1, Apple a2) ->
                a1.getWeight().compareTo(a2.getWeight());
    }

//    (String s) -> s.length()
//    (Apple a) -> a.getWeight() > 150
//    (int x, int y) -> {
//        System.out.println("Result:");
//        System.out.println(x + y);
//    }
//    () -> 42
//            (Apple a1, Apple a2) ->
//            a1.getWeight().compareTo(a2.getWeight());

    public String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            return p.process(br);
//            return br.readLine();
        }
    }

    @FunctionalInterface
    public interface BufferedReaderProcessor {
        String process(BufferedReader b) throws IOException;
    }

    public String processFile2() throws IOException {
        String oneLine = processFile((BufferedReader br) -> br.readLine());
        String oneLineMethodReference = processFile(BufferedReader::readLine);
        String twoLines = processFile((BufferedReader br) -> br.readLine() + br.readLine());
        return oneLine;
    }


    /*Predicate*/
    @FunctionalInterface
    public interface PredicateCustom<T> {
        boolean test(T t);
    }

    public <T> List<T> filter(List<T> list, PredicateCustom<T> p) {
        List<T> results = new ArrayList<>();
        for (T t : list) {
            if (p.test(t)) {
                results.add(t);
            }
        }
        return results;
    }

    List<String> listOfStrings = Arrays.asList("a", "b", "A", "B");
    PredicateCustom<String> nonEmptyStringPredicateCustom = (String s) -> !s.isEmpty();
    List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicateCustom);


    /*Consumer*/
    @FunctionalInterface
    public interface Consumer<T> {
        void accept(T t);
    }

    public <T> void forEach(List<T> list, Consumer<T> c) {
        for (T t : list) {
            c.accept(t);
        }
    }

    public void consumerExample() {
        forEach(
                Arrays.asList(1, 2, 3, 4, 5),
//                (Integer i) -> System.out.println(i)
                System.out::println
        );
    }

    /*Function*/
    @FunctionalInterface
    public interface FunctionCustom<T, R> {
        R apply(T t);
    }

    public <T, R> List<R> mapCustom(List<T> list, FunctionCustom<T, R> f) {
        List<R> result = new ArrayList<>();
        for (T t : list) {
            result.add(f.apply(t));
        }
        return result;
    }

    List<Integer> l = this.mapCustom(
            Arrays.asList("lambdas", "in", "action"),
            String::length
//            (String s) -> s.length()
    );

    /* 기본형 자료 타입 , 기본형 특화 */
    public interface IntPredicate {
        boolean test(int t);
    }

    public void primitiveType() {
        IntPredicate evenNumbers = (int i) -> i % 2 == 0;
        boolean test = evenNumbers.test(1000);// 박싱 없음

        PredicateCustom<Integer> oddNumbers = (Integer i) -> i % 2 == 0;
        boolean test2 = oddNumbers.test(1000);// 박싱 발생
    }


//    Object o = () -> {
//        System.out.println("Hello World");
//    }

    Runnable r = () -> {
        System.out.println("Hello World");
    };

    Object o = (Runnable) () -> {
        System.out.println("Hello World");
    };


    public void execute(Runnable runnable) {
        runnable.run();
    }

    public void execute(Action action) {
        action.act();
    }

    @FunctionalInterface
    interface Action {
        void act();
    }

    public void test() {
//        execute(() -> {});
        execute((Action) () -> {
        });
        execute((Runnable) () -> {
        });
    }

    List<Apple> inventory = Arrays.asList(new Apple(80, GREEN),
            new Apple(155, GREEN),
            new Apple(120, RED));

    public void 형식추론() {
        List<Apple> greenApples =
                filter(inventory, apple -> GREEN.toString().equals(apple.getColor()));

        Comparator<Apple> c =
                (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());

        Comparator<Apple> c2 =
                (a1, a2) -> a1.getWeight().compareTo(a2.getWeight());

        Comparator<Apple> c3 =
                comparing(Apple::getWeight);
    }

    public void 지역변수사용() {
        int portNumber = 8080;
        Runnable r = () -> System.out.println(portNumber);

//        portNumber = 8888;
//        final로 선언된 변수와 똑같이 사용되어야 한다
//        final 변수 혹은 유사 final의 지역 변수만 캡처 가능

//        인스턴스 변수는 힙에 저장, 지역변수는 스택에 위치
//        힙에 들어가있는 인스턴스 변수가 사라졌는데, 람다 실행하는 스레드에서 해당 변수에 접근 못하도록
    }

    private boolean isValidName(String string) {
        return Character.isUpperCase(string.charAt(0));
    }

    public void 메서드참조() {
        inventory.sort((Apple a1, Apple a2) ->
                a1.getWeight().compareTo(a2.getWeight()));

        inventory.sort(comparing(Apple::getWeight));

        List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
        filter(words, this::isValidName);

        List<String> str = Arrays.asList("a", "b", "A", "B");
        str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        str.sort(String::compareToIgnoreCase);


        ToIntFunction<String> stringToInt = (String s) -> Integer.parseInt(s);
        FunctionCustom<String, Integer> stringToInteger = Integer::parseInt;

        BiPredicate<List<String>, String> contains
                = (list, element) -> list.contains(element);
        BiPredicate<List<String>, String> contains2 = List::contains;


//        Predicate<String> startsWithNumber =
//                (String string) -> this.startsWithNumber(string);
//        Predicate<String> startsWithNumber2 = this::startsWithNumber;
    }

    public void 생성자참조() {
        Supplier<Apple> c1 = Apple::new;
        Apple a1 = c1.get();

        Supplier<Apple> c2 = () -> new Apple();
        Apple a2 = c2.get();

        FunctionCustom<Integer, Apple> c3 = Apple::new;
        Apple a3 = c3.apply(110);

        FunctionCustom<Integer, Apple> c4 = (weight) -> new Apple(weight);
        Apple a4 = c4.apply(110);
    }

    List<Integer> weights = Arrays.asList(7, 3, 4, 10);
    List<Apple> apples = mapCustom(weights, Apple::new);

    public List<Apple> mapApple(List<Integer> list, FunctionCustom<Integer, Apple> f) {
        List<Apple> result = new ArrayList<>();
        for (Integer i : list) {
            result.add(f.apply(i));
        }
        return result;
    }

    BiFunction<Color, Integer, Apple> c3 = Apple::new;
    Apple apple = c3.apply(GREEN, 110);

    BiFunction<String, Integer, Apple> c4 =
            (color, weight) -> new Apple(Color.valueOf(color), weight);
    Apple apple4 = c4.apply(String.valueOf(GREEN), 110);

//    static Map<String, Function<Integer, Fruit>> map = new HashMap<>();
//    static {
//        map.put("apple", Apple::new);
//        map.put("orange", Orange::new);
//    }
//
//    public static Fruit giveMeFruit(String fruit, Integer weight) {
//        return map.get(fruit.toLowerCase())
//                .apply(weight);
//    }

    public interface TriFunction<T, U, V, R>{
        R apply(T t, U u, V v);
    }

//    TriFunction<Integer, Integer, Integer, Color> colorFactory = Color::new;

    public void 람다와메서드참조활용하기(){
        inventory.sort(comparing(Apple::getWeight));

        List<Apple> inventory2 = new ArrayList<>();
        inventory2.add(new Apple(150, Color.valueOf("red")));
        inventory2.add(new Apple(120, Color.valueOf("green")));
        inventory2.add(new Apple(200, Color.valueOf("red")));
        inventory2.add(new Apple(110, Color.valueOf("yellow")));

        inventory2.sort(new Comparator<Apple>() {
            public int compare(Apple a1, Apple a2){
                return a1.getWeight().compareTo(a2.getWeight());
            }
        });

        inventory2.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
        inventory2.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));
        Comparator<Apple> c = Comparator.comparing((Apple a) -> a.getWeight());
        inventory2.sort(comparing(apple -> apple.getWeight()));
        inventory2.sort(comparing(Apple::getWeight));

    }
    public class AppleComparator implements Comparator<Apple> {
        @Override
        public int compare(Apple a1, Apple a2) {
            return a1.getWeight().compareTo(a2.getWeight());
        }
    }


    public void 람다표현식조합유용한메서드(){
        Comparator<Apple> c = Comparator.comparing(Apple::getWeight);

        inventory.sort(comparing(Apple::getWeight).reversed());

        inventory.sort(comparing(Apple::getWeight)
                .reversed()
                .thenComparing(Apple::getColor));

        Predicate<Apple> redApple = apple -> "red".equals(apple.getColor());
        Predicate<Apple> notRedApple = redApple.negate();
        Predicate<Apple> readAndHeavyApple
                = redApple.and(apple -> apple.getWeight() > 150);
        Predicate<Apple> readAndHeavyAppleOrGreen
                = redApple.and(apple -> apple.getWeight() > 150)
                .or(apple -> GREEN.toString().equals(apple.getColor()));



        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = f.andThen(g);
        int result = h.apply(1);

        Function<Integer, Integer> f2 = x -> x + 1;
        Function<Integer, Integer> g2 = x -> x * 2;
        Function<Integer, Integer> h2 = f2.compose(g2);
        int result2 = h2.apply(1);
    }

    public class Letter{
        public static String addHeader(String text) {
            return "From Raoul, Mario and Alan: " + text;
        }
        public static String addFooter(String text){
            return text + " Kind regards";
        }

        public static String checkSpelling(String text){
            return text.replaceAll("labda", "lambda");
        }

        Function<String, String> addHeader = Letter::addHeader;
        Function<String, String> transformationPipeline =
                addHeader.andThen(Letter::checkSpelling)
                        .andThen(Letter::addFooter);
    }

    public void 수학에적용(){

    }
//    public double integrate((double -> double) f, double a, double b) {
//
//    }
    public double integrate(DoubleFunction<Double> f, double a, double b) {
        return (f.apply(a) + f.apply(b)) * (b - a) / 2.0;
    }

    public double integrate2(DoubleUnaryOperator f, double a, double b) {
        return (f.applyAsDouble(a) + f.applyAsDouble(b)) * (b - a) / 2.0;
    }




}
