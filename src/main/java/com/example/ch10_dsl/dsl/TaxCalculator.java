package com.example.ch10_dsl.dsl;

import com.example.ch10_dsl.dsl.model.Order;
import com.example.ch10_dsl.dsl.model.Tax;

import java.util.function.DoubleUnaryOperator;

public class TaxCalculator {


    public static double calculateOld(Order order, boolean useRegional,
                                      boolean useGeneral, boolean useSurcharge) {
        double value = order.getValue();
        if (useRegional) value = Tax.regional(value);
        if (useGeneral) value = Tax.general(value);
        if (useSurcharge) value = Tax.surcharge(value);
        return value;
    }

    private boolean useRegional;
    private boolean useGeneral;
    private boolean useSurcharge;

    public TaxCalculator withTaxRegional() {
        useRegional = true;
        return this;
    }

    public TaxCalculator withTaxGeneral() {
        useGeneral = true;
        return this;
    }

    public TaxCalculator withTaxSurcharge() {
        useSurcharge = true;
        return this;
    }

    public double calculateOld(Order order) {
        return calculateOld(order, useRegional, useGeneral, useSurcharge);
    }

    public DoubleUnaryOperator taxFunction = d -> d;

    public TaxCalculator with(DoubleUnaryOperator f) {
        taxFunction = taxFunction.andThen(f);
        return this;
    }

    public double calculate(Order order) {
        return taxFunction.applyAsDouble(order.getValue());
    }


    public static void main(String[] args) {
        Order order = new Order();
        double value = new TaxCalculator().withTaxRegional()
                .withTaxSurcharge()
                .calculateOld(order);

        double value2 = new TaxCalculator().with(Tax::regional)
                .with(Tax::surcharge)
                .calculate(order);

    }
}


