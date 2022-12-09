package com.paytm.pg.aggregator.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paytm.pg.aggregator.Processor.ConsumeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;


@Slf4j
@ComponentScan
@Configuration
public class Kafka {

    @Autowired
    ConsumeProcessor consumeProcessor;

    @KafkaListener(topics = "test", groupId = "Default_Consunmer")
    public void listen(String message,
                       @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) throws ParseException, JSONException, JsonProcessingException {
//        log.info(message);
//        log.info("Kafka key is -> "+key);
        consumeProcessor.saveDto(message,key);

    }
}
