package com.example.ch10_dsl.dsl.methodchain;

import com.example.ch10_dsl.dsl.model.Stock;
import com.example.ch10_dsl.dsl.model.Trade;

public class StockBuilder {
    private final MethodChainingOrderBuilder builder;
    private final Trade trade;
    private final Stock stock = new Stock();

    private StockBuilder(MethodChainingOrderBuilder builder,
                         Trade trade,
                         String symbol) {
        this.builder = builder;
        this.trade= trade;
        stock.setSymbol(symbol);
    }

    public TradeBuilderWithStock on(String market){
        stock.setMarket(market);
        trade.setStock(stock);
        return new TradeBuilderWithStock(builder, trade);
    }
}
