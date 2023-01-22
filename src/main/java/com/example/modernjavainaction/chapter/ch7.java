package com.example.modernjavainaction.chapter;

import java.util.stream.LongStream;
import java.util.stream.Stream;

/* 병렬 데이터 처리와 성능 */
public class ch7 {

    public long iterativeSum(long n) {
        long result = 0;
        for (long i = 0L; i <= n; i++) {
            result += i;
        }
        return result;
    }

    public long sequentialSum(long n){
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    public long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public void 병렬스트림(){

    }

    public long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).forEach(accumulator::add);
        return accumulator.total;
    }

    public static long sideEffectParallelSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
        return accumulator.total;
    }

    public static void main(String[] args) {
        System.out.println(sideEffectParallelSum(10_000_000L));
        System.out.println(sideEffectParallelSum(10_000_000L));
        System.out.println(sideEffectParallelSum(10_000_000L));
        System.out.println(sideEffectParallelSum(10_000_000L));
        System.out.println(sideEffectParallelSum(10_000_000L));
    }

    static class Accumulator {
        public long total = 0;
        public void add(long value ) { total += value;}
    }


    public void 포크조인프레임워크(){

    }

    public void Spliterator(){

    }

    public int countWordsIteratively(String s){
        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            }else {
                if (lastSpace) counter++;
                lastSpace = false;
            }
        }
        return counter;
    }
}
