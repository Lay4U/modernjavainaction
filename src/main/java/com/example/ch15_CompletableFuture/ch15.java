package com.example.ch15_CompletableFuture;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.IntConsumer;

public class ch15 {
    void concurrency() {
//        int f(int x);
//        int g(int x);
//
//        int y = f(x);
//        int z = g(x);
//        System.out.println(y + z);
    }

    class ThreadExample {
        public static void main(String[] args) throws InterruptedException {
            int x = 1337;
            Result result = new Result();

            Thread t1 = new Thread(() -> {
                result.left = f(x);
            });
            Thread t2 = new Thread(() -> {
                result.right = g(x);
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println(result.left + result.right);
        }
    }

    class ExecuterServiceExample {
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            int x = 1337;

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            Future<Integer> y = executorService.submit(() -> f(x));
            Future<Integer> z = executorService.submit(() -> g(x));
            System.out.println(y.get() + z.get());

            executorService.shutdown();
        }
    }

    class CallbackStyleExample {
        public static void main(String[] args) {
            int x = 1337;
            Result result = new Result();

            f(x, (int y) -> {
                result.left = y;
                System.out.println((result.left + result.right));
            });

            g(x, (int z) -> {
                result.right = z;
                System.out.println((result.left + result.right));
            });
        }

        public static void f(int x, IntConsumer dealWightResult) {

        }

        public static void g(int x, IntConsumer dealWightResult) {

        }
    }


    public class ScheduledExecutorServiceExample {

        public void sleep() throws InterruptedException {
            work1();
            Thread.sleep(10000); //자는동안 아무것도 하지 않는다.
            work2();
        }

        public static void main(String[] args) {
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            work1();
            scheduledExecutorService.schedule(
                    ScheduledExecutorServiceExample::work2, 10, TimeUnit.SECONDS);
            //10초 뒤에 실행될 수 있도록 큐에 추가하고, 다른 일을 하러 간다.

            scheduledExecutorService.shutdown();
            //태스크를 블록하는것 보다는 다음 작업을 태스크로 제출해놓고 현재 태스크는 종료하는것이 바람직함.
        }

        public static void work1() {
            System.out.println("work1");
        }

        public static void work2() {
            System.out.println("work2");
        }

    }

    private static int g(int x) {
        return x + 5;
    }

    private static int f(int x) {
        return x + 2;
    }

    private static class Result {
        private int left;
        private int right;
    }

    public void 박스와채널모델(){
//        int t = p(x);
//        System.out.println(r(q1(t), q2(t)));
//
//        int t = p(x);
//        Future<Integer> a1 = executorService.submit(() -> q1(t));
//        Future<Integer> a2 = executorService.submit(() -> q2(t));
//        System.out.println(r(a1.get(), a2.get()));
//
//
//        Function<Integer, Integer> myfun = add1.andThen(dble);
//
//        p.thenBoth(q1, q2).thenCombine(r)
    }

    public class CFComplete{
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            int x = 1337;

            CompletableFuture<Integer> a = new CompletableFuture<>();
            executorService.submit(() -> a.complete(f(x)));
            int b = g(x);
            System.out.println(a.get() + b);

            executorService.shutdown();
        }
    }

    public class CFCompete2 {
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            int x = 1337;

            CompletableFuture<Integer> b = new CompletableFuture<>();
            executorService.submit(() -> b.complete(g(x)));
            int a = f(x);
            System.out.println(a + b.get());

            executorService.shutdown();
        }
    }

    public class CfCombine{
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            int x = 1337;

            CompletableFuture<Integer> a = new CompletableFuture<>();
            CompletableFuture<Integer> b = new CompletableFuture<>();
            CompletableFuture<Integer> c = a.thenCombine(b, (y, z) -> y + z);
            executorService.submit(() -> a.complete(f(x)));
            executorService.submit(() -> b.complete(g(x)));

            System.out.println(c.get());
            executorService.shutdown();

        }
    }

    /*구독 발행 리액티브 프로그래밍*/

}
