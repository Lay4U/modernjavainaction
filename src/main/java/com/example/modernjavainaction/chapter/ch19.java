package com.example.modernjavainaction.chapter;

import org.aspectj.weaver.ast.Expr;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ch19 {
    public void stuff() {
        Function<String, Integer> strToInt = Integer::parseInt;
    }

    static double converter(double x, double f, double b) {
        return x * f + b;
    }

    static DoubleUnaryOperator curriedConverter(double f, double b) {
        return (double x) -> x * f + b;
    }

    public static class TrainJourney {
        public int price;
        public TrainJourney onward;

        public TrainJourney(int price, TrainJourney onward) {
            this.price = price;
            this.onward = onward;
        }
    }

    static TrainJourney link(TrainJourney a, TrainJourney b) {
        if (a == null) return b;
        TrainJourney t = a;
        while (t.onward != null) {
            t = t.onward;
        }
        t.onward = b;
        return a;
    }

    static TrainJourney append(TrainJourney a, TrainJourney b) {
        return a == null ? b : new TrainJourney(a.price, append(a.onward, b));
    }

    public static class Tree {
        private String key;
        private int val;
        private Tree left, right;

        public Tree(String key, int val, Tree left, Tree right) {
            this.key = key;
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static class TreeProcessor {
        public static int lookup(String k, int defaultValue, Tree t) {
            if (t == null) return defaultValue;
            if (k.equals(t.key)) return t.val;
            return lookup(k, defaultValue, k.compareTo(t.key) < 0 ? t.left : t.right);
        }
    }

    public static void update(String k, int newval, Tree t) {
        if (t == null) {
            // 새로운 노드 추가
        } else if (k.equals(t.key)) {
            t.val = newval;
        } else {
            update(k, newval, k.compareTo(t.key) < 0 ? t.left : t.right);
        }
    }

    public static Tree fupdate(String k, int newVal, Tree t) {
        return (t == null) ?
                new Tree(k, newVal, null, null) :
                k.equals(t.key) ?
                        new Tree(k, newVal, t.left, t.right) :
                        k.compareTo(t.key) < 0 ?
                                new Tree(t.key, t.val, fupdate(k, newVal, t.left), t.right) :
                                new Tree(t.key, t.val, t.left, fupdate(k, newVal, t.right));
    }

    public static Stream<Integer> primes(int n) {
        return Stream.iterate(2, i -> i + 1)
                .filter(ch19::isPrime)
                .limit(n);
    }

    public static boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt(candidate);
        return IntStream.rangeClosed(2, candidateRoot).noneMatch(i -> candidate % i == 0);
    }

    static IntStream numbers() {
        return IntStream.iterate(2, n -> n + 1);
    }

    static int head(IntStream numbers) {
        return numbers.findFirst().getAsInt();
    }

    static IntStream tail(IntStream numbers) {
        return numbers.skip(1);
    }

    static IntStream primes(IntStream numbers) {
        int head = head(numbers);
        return IntStream.concat(
                IntStream.of(head),
                primes(tail(numbers).filter(n -> n % head != 0))
        );
    }

    /*
     * def numbers(n:  Int): Stream[Int] = n #:: numbers(n + 1)
     *
     * def primes(numbers: Stream[Int]): Stream[Int] = {
     *   numbers.head #:: primes(numbers.tail.filter(_ % numbers.head != 0))
     * */

    interface MyList<T> {
        T head();

        MyList<T> tail();

        default boolean isEmpty() {
            return true;
        }
    }

    class MyLinkedList<T> implements MyList<T> {


        private final T head;
        private final MyList<T> tail;

        public MyLinkedList(T head, MyList<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public MyList<T> tail() {
            return tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    class Empty<T> implements MyList<T> {

        @Override
        public T head() {
            throw new UnsupportedOperationException();
        }

        @Override
        public MyList<T> tail() {
            throw new UnsupportedOperationException();
        }
    }


    MyList<Integer> l =
            new MyLinkedList<>(5, new MyLinkedList<>(10, new Empty<>()));

    static class LazyList<T> implements MyList<T> {
        final T head;
        final Supplier<MyList<T>> tail;

        public LazyList(T head, Supplier<MyList<T>> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public MyList<T> tail() {
            return tail.get();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

//        public MyList<T> filter(Predicate<T> p) {
//            return isEmpty() ?
//                    this :
//                    p.test(head()) ?
//                            new LazyList<>(head(), () -> tail().filter(p)) :
//                            tail().filter(p);
//        }
//
//        public static MyList<Integer> primes(MyList<Integer> numbers) {
//            return new LazyList<>(
//                    numbers.head(),
//                    () -> primes(
//                            numbers.tail()
//                                    .filter(n -> n % numbers.head() != 0)
//                    )
//            )
//        }


    }

    public static LazyList<Integer> from(int n) {
        return new LazyList<Integer>(n, () -> from(n + 1));
    }

    public static void test() {
        LazyList<Integer> numbers = from(2);
        Integer two = numbers.head();
        Integer three = numbers.tail().head();
        Integer four = numbers.tail().tail().head();
    }

}