package com.example.modernjavainaction.custom;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.*;

//ch6.5.2 커스텀 ToListCollector 구현
@Deprecated
public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {
    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new; //수집 연산의 시발점
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return List::add; // 탐색한 항목을 누적하고 바로 누적자를 고친다.
    }


    @Override
    public Function<List<T>, List<T>> finisher() {
        return Function.identity(); //항등 함수
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (list1, list2) -> {  // 두번재 콘텐츠와 합쳐서 첫 번째 누적자를 고친다.
            list1.addAll(list2);    // 변경된 첫 번째 누적자를 반환한다.
            return list1;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
        //컬렉터의 플래그
    }
}
