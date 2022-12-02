package com.paytm.pg.aggregator.Model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class Order {

    Date gmtCreate;
    Date gmtModified;
    String ip_role_id;
    String bizId;
    String ip_role_type;
    String entity_type;
    String entity_sub_type;
    String state;
    long total_count;
    long total_amount;
    String total_amount_currency;
    long total_commission;
    String total_commission_currency;
    long total_commission_tax;
    String total_commission_tax_currency;
    long total_pc_fee;
    String total_pc_fee_currency;
    int total_pc_tax;
    String total_pc_tax_currency;
    String extend_info;
    long calculated_on_date;
    Date start_date;
    Date end_date;
    String time_unit;
    long dt;
    String pay_method;
    Date partition_date;

}
