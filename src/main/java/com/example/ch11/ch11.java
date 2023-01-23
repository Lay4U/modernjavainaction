package com.example.ch11;

public class ch11 {
    public String getCarInsurance(Person person){
        return person.getCar().getInsurance().getName(); // NPE
    }

    public String getCarInsuranceName(Person person){
        if(person != null){
            Car car = person.getCar();
            if(car != null){
                Insurance insurance = car.getInsurance();
                if(insurance != null){
                    return insurance.getName();
                }
            }
        }
        return "Unknown";
    }

    public String getCarInsuranceName2(Person person){
        if(person == null){
            return "Unknown";
        }
        Car car = person.getCar();
        if(car == null){
            return "Unknown";
        }
        Insurance insurance = car.getInsurance();
        if(insurance == null){
            return "Unknown";
        }
        return insurance.getName();
    }


}
