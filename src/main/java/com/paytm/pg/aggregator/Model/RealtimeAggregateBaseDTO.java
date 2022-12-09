package com.paytm.pg.aggregator.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
 @Data
    @NoArgsConstructor
    @AllArgsConstructor
 @ToString
    public class RealtimeAggregateBaseDTO {

        private Date orderCreatedTime;
        private Date pushedTime;
        private String bizOrderId;
        private String dtoType;
        private String ipRoleId;
        private String feeOrderId;
        private String orderStatus;
        private String orderType;
        private String subOrderType;
        private String payMethod;
        private String productCode;
        private CurrencyAmount orderAmount;
        private CurrencyAmount pcfFee;
        private CurrencyAmount pcfFeeTax;
        private CurrencyAmount mdrFee;
        private CurrencyAmount mdrFeeTax;

    }

