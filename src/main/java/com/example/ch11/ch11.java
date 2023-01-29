package com.example.ch11;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class ch11 {
//    public String getCarInsurance(Person person){
//        return person.getCar().getInsurance().getName(); // NPE
//    }
//
//    public String getCarInsuranceName(Person person){
//        if(person != null){
//            Car car = person.getCar();
//            if(car != null){
//                Insurance insurance = car.getInsurance();
//                if(insurance != null){
//                    return insurance.getName();
//                }
//            }
//        }
//        return "Unknown";
//    }
//
//    public String getCarInsuranceName2(Person person){
//        if(person == null){
//            return "Unknown";
//        }
//        Car car = person.getCar();
//        if(car == null){
//            return "Unknown";
//        }
//        Insurance insurance = car.getInsurance();
//        if(insurance == null){
//            return "Unknown";
//        }
//        return insurance.getName();
//    }

    public void Optional_pattern(){
        Car car = new Car();
        Optional<Car> optCar = Optional.empty();
        Optional<Car> optCar2 = Optional.of(car);
        Optional<Car> optCar3 = Optional.ofNullable(car);


        Insurance insurance = new Insurance();
        String name = null;
        if(insurance != null){
            name = insurance.getName();
        }

        Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
        Optional<String> name2 = optInsurance.map(Insurance::getName);

        Person person = new Person();




    }

//    public String getCarInsuranceName(Person person){
//        return person.getCar().getInsurance().getName();

//    Optional<Person> optPerson = Optional.of(person);
//            optPerson.map(Person::getCar)
//                .map(Car::getInsurance)
//                .map(Insurance::getName);
//    }
    public String getCarInsuranceName(Optional<Person> person){
        return person.flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("Unknown");
    }

    public Set<String> getCarInsuranceNames(List<Person> persons){
        return persons.stream()
                .map(Person::getCar)
                .map(optCar -> optCar.flatMap(Car::getInsurance))
                .map(optIns -> optIns.map(Insurance::getName))
                .flatMap(Optional::stream)
                .collect(toSet());
    }

    public Optional<Insurance> nullSafeFIndCheapestInsurance(
            Optional<Person> person, Optional<Car> car){
//        if(person.isPresent() && car.isPresent()){
//            return Optional.of(findCheapestInsurance(person.get(), car.get()));
//        }else {
//            return Optional.empty();
//        }

        return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
    }

    private Insurance findCheapestInsurance(Person person, Car car) {
        return null;
    }

    public void keepGoing(){
        Insurance insurance = new Insurance();
        if(insurance != null && "CambridgeInsurance".equals(insurance.getName())){
            System.out.println("ok");
        }

        Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
        optInsurance.filter(insurance2 -> "CambridgeInsurance".equals(insurance2.getName()))
                .ifPresent(x -> System.out.println("ok"));


    }
    public String getCarInsurance(Optional<Person> person, int minAge){
        return person.filter(p -> p.getAge() >= minAge)
                .flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("Unknown");
    }


    class OptionalExample {
        Map<String,Object> map = new HashMap<>();
        Optional<Object> value = Optional.ofNullable(map.get("key"));

        public static Optional<Integer> stringToInt(String s) {
            try{
                return Optional.of(Integer.parseInt(s));
            } catch(NumberFormatException e){
                return Optional.empty();
            }
        }


        public int readDuration(Properties props, String name){
                String value = props.getProperty(name);
                if(value != null){
                    try{
                        int i = Integer.parseInt(value);
                        if(i > 0){
                            return i;
                        }
                    } catch (NumberFormatException nfe) { }
                }
                return 0;
        }
    }

    public int readDurationOptional(Properties props, String name){
        return Optional.ofNullable(props.getProperty(name))
                .flatMap(OptionalExample::stringToInt)
                .filter(i -> i > 0)
                .orElse(0);
    }









}
