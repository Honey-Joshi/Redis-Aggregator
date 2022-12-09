package com.paytm.pg.aggregator.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString
@Builder
public class Order {
    String orderType;
    private String bizOrderId;
    private String ipRoleId;
    private String payMethod;
    SubOrderType subOrderTyp;
//    private CurrencyAmount totalAmount;
//    private CurrencyAmount totalPcfFee;
//    private CurrencyAmount totalPcfFeeTax;
//    private CurrencyAmount totalMdrFee;
//    private CurrencyAmount totalMdrFeeTax;
}
