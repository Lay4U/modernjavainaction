package com.example.modernjavainaction.chapter;

import com.example.modernjavainaction.domain.Car;
import com.example.modernjavainaction.domain.Dish;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import static com.example.modernjavainaction.chapter.ch4.menu;
import static java.util.stream.Collectors.groupingBy;

/* 람다를 이용한 도메인 전용 언어*/
public class ch10 {
    // 메뉴에서 400 칼로리 이하의 모든 요리를 찾으시오

/*    while (block != null){
        read(block, buffer)
        for (every record in buffer) {
            if (record.calorie < 400){
                System.out.print(record.name);
            }
        }
        block = buffer.next();
    }*/

    // 락, I/O, 디스크 할당 등과 같은 지식이 필요하므로 구현이 어렵다...
    // 애플리케이션 수준이 아니라 시스템 수준의 개념을 다루어야 한다

    // DSL을 이용해 데이터베이스를 조작하여 값을 가져오자
    // -> 외부적 DSL을 사용한다.

    // stream으로 내부적 DSL을 사용한다.
    public void internalDSL() {
        menu.stream().filter(d -> d.getCalories() < 400).map(Dish::getName).forEach(System.out::println);
    }

    public void streamApi() throws IOException {
        ArrayList<String> errors = new ArrayList<>();
        int errorCount = 0;
        String fileName = "temp";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();
        while (errorCount < 40 && line != null) {
            if (line.startsWith("ERROR")) {
                errors.add(line);
                errorCount++;
            }
        }
        line = bufferedReader.readLine();

        List<String> errors2 = Files.lines(Paths.get(fileName))
                .filter(line2 -> line2.startsWith("ERROR"))
                .limit(40)
                .toList();

//        Map<String, Map<Color, List<Car>>> carsByBrandAndColor =
//                cars.stream()
//                .collect(groupingBy(Car::getBrand,
//                        groupingBy(Car::getColor)));
//
//        Comparator<Person> comparator =
//                comparing(Person::getAge)
//                        .thenComparing(Person::getName);
//
//        Collector<? super Car, ?, Map<Brand, Map<Color, List<Car>>>> carGroupingCollector =
//                groupingBy(Car::getBrand, groupingBy(Car::getColor));
//

        class GroupingBuilder<T, D, K> {
            private final Collector<? super T, ?, Map<K, D>> collector;

            private GroupingBuilder(Collector<? super T, ?, Map<K, D>> collector) {
                this.collector = collector;
            }

            public Collector<? super T, ?, Map<K, D>> get() {
                return collector;
            }

            public <J> GroupingBuilder<T, Map<K, D>, J> after(Function<? super T, ? extends J> classifier) {
                return new GroupingBuilder<>(groupingBy(classifier, collector));
            }

            public static <T, D, K> GroupingBuilder<T, List<T>, K> groupOn(Function<? super T, ? extends K> classifier) {
                return new GroupingBuilder<>(groupingBy(classifier));
            }

//            Collector<? super Car, ?, Map<Brand, Map<Color, List<Car>>>>
//            carGroupingCollector =
//                    groupOn(Car::getColor).after(Car::getBrand).get()



        }


    }


}


