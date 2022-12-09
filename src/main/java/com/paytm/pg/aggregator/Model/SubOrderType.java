package com.paytm.pg.aggregator.Model;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@ToString
public class SubOrderType {

    private String subOrderType;
    private long count;
    private CurrencyAmount orderAmount;
    private CurrencyAmount pcfFee;
    private CurrencyAmount pcfFeeTax;
    private CurrencyAmount mdrFee;
    private CurrencyAmount mdrFeeTax;
}
