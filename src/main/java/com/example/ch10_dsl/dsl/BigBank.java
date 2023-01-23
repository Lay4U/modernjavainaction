package com.example.ch10_dsl.dsl;

import com.example.ch10_dsl.dsl.methodchain.MethodChainingOrderBuilder;
import com.example.ch10_dsl.dsl.model.Order;
import com.example.ch10_dsl.dsl.model.Trade;
import com.example.ch10_dsl.dsl.model.Stock;

import static com.example.ch10_dsl.dsl.NestedFunctionOrderBuilder.*;

public class BigBank {
    public void domainModel(){
        Order order = new Order();
        order.setCustomer("BigBank");

        Trade trade1 = new Trade();
        trade1.setType(Trade.Type.BUY);

        Stock stock1 = new Stock();
        stock1.setSymbol("IBM");
        stock1.setMarket("NYSE");

        trade1.setStock(stock1);
        trade1.setPrice(125.00);
        trade1.setQuantity(80);
        order.addTrade(trade1);

        Trade trade2 = new Trade();
        trade2.setType(Trade.Type.BUY);

        Stock stock2 = new Stock();
        stock2.setSymbol("GOOGLE");
        stock2.setMarket("NASDAQ");

        trade2.setStock(stock2);
        trade2.setPrice(375.00);
        trade2.setQuantity(50);
        order.addTrade(trade2);
    }

    public void DSLChaining(){
        Order order = MethodChainingOrderBuilder.forCustomer("BigBank")
                .buy(80)
                .stock("IBM")
                .on("NYSE")
                .at(125.00)
                .sell(50)
                .stock("GOOGLE")
                .on("NASDAQ")
                .at(375.00)
                .end();
    }

    public void NestedFunction(){
        Order order = order("BigBank",
                buy(80,
                        stock("IBM", on("NYSE")), at(125.00)),
                sell(50,
                        stock("GOOGLE", on("NASDAQ")), at(375.00))
        );
    }

    public void labmdaOrder() {
        Order order = LambdaOrderBuilder.order(o -> {
            o.forCustomer(" BigBank ");
            o.buy(t -> {
                t.quantity(80);
                t.price( 125.00 );
                t.stock( s -> {
                    s.symbol("IBM");
                    s.market("NYSE");
                });
            });
            o.sell(t -> {
                t.quantity(50);
                t.price( 375.00 );
                t.stock( s -> {
                    s.symbol("GOOGLE");
                    s.market("NASDAQ");
                });
            });
        });
    }

    public void combination(){
        Order order =
                MixedBuilder.forCustomer ("BigBank",
                        MixedBuilder.buy(t -> t.quantity(80)
                                .stock("IBM")
                                .on("NYSE")
                                .at(125.00)),
                        MixedBuilder.sell(t -> t.quantity(50)
                                .stock("GOOGLE")
                                .on("NASDAQ")
                                .at(375.00)));
    }


}
