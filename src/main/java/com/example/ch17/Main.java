package com.example.ch17;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        getTemperature( "New York").subscribe( new TempSubscriber() );

        getCelsiusTemperature("New York").subscribe( new TempSubscriber() );

        Observable<TempInfo> observable = getTemperatureObserver("New York");
        observable.blockingSubscribe(new TempObserver());

        Observable<TempInfo> observable2 = getCelsiusTemperatures("New York", "Chicago", "San Francisco");
        observable2.blockingSubscribe(new TempObserver());



    }

    private static Flow.Publisher<TempInfo> getTemperature(String town) {
        return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
    }

    private static Flow.Publisher<TempInfo> getCelsiusTemperature(String town) {
        return subscriber -> {
            TempProcessor processor = new TempProcessor();
            processor.subscribe(subscriber);
            processor.onSubscribe(new TempSubscription(processor, town));

        };
    }

    private static Observable<TempInfo> getTemperatureObserver(String town) {
        return Observable.create(emitter -> {
            Observable.interval(1, TimeUnit.SECONDS)
                    .subscribe(i -> {
                        if (!emitter.isDisposed()) {
                            if ( i>= 5){
                                emitter.onComplete();
                            } else {
                                try{
                                    emitter.onNext(TempInfo.fetch(town));
                                } catch (Exception e){
                                    emitter.onError(e);
                                }
                            }
                        }
                    });
        });
    }

    public static Observable<TempInfo> getCelsiusTemperatureObserver(String town){
        return getTemperatureObserver( town )
                .map( temp -> new TempInfo ( temp.getTown(), (temp.getTemp() - 32) * 5 / 9));
    }

    public static Observable<TempInfo> getCelsiusTemperatures(String... towns){
        return Observable.merge(Arrays.stream(towns)
                .map(Main::getCelsiusTemperatureObserver)
                .toList());
    }


}
