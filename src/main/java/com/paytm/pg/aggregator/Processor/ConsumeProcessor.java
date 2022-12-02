package com.paytm.pg.aggregator.Processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONException;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Configuration
@Slf4j
public class ConsumeProcessor {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisScript redisScript;
    public void saveDto(String message, String key) throws ParseException, JSONException {

        Object k;
        String k_key="";
            JSONObject jsonObject = new JSONObject(message);
            k = jsonObject.get("Primary_Key");
            k_key = k.toString();
            log.info("Primary_Key obtained from JSON -> "+k_key);
            StopWatch st = new StopWatch("abcd");
            st.start();

 //         network call to check
            String s = redisTemplate.execute(redisScript, List.of(k_key),message,key).toString();
            log.info(" LUA Response-> "+s);

            st.stop();

//            network call to set the flag to 1;
            log.info("Total time taken in ms -> "+st.getTotalTimeMillis());
            log.info("Total time taken in nanoSeconds -> "+st.getTotalTimeNanos());



    }
}
