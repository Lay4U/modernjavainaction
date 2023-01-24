package com.example.modernjavainaction.chapter;

import java.util.concurrent.*;

public class ch16 {
    public void Future단순활용(){
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new Callable<Double>() {
            @Override
            public Double call() {
                return doSOmeLongComputation();
            }
        });
        doSomethingElse();
        try{
            Double result = future.get(1, TimeUnit.SECONDS);
        }catch (ExecutionException e){
            throw launderThrowable(e.getCause());
        }catch( InterruptedException e){

        }catch (TimeoutException e){

        }
    }
}
