package com.paytm.pg.aggregator.Controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@Slf4j
public class AggregatorRestController {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    RedisTemplate redisTemplate;

    private static final String TOPIC = "test";

    // Publish messages using the GetMapping
    @GetMapping("/publish/{message}")
    public String publishMessage(@PathVariable("message")
                                 final String message)
    {

        // Sending the message
        JSONObject j = new JSONObject("{\"Primary_Key\":\"101_ec2\",\"Instant\":{\"commision_tax\":1555.2,\"pc_fees\":4320,\"pc_tax\":734.4,\"total_count\":864,\"total_amount\":32400,\"commision\":17280},\"Acquiring\":{\"commision_tax\":1555.2,\"pc_fees\":4320,\"pc_tax\":734.4,\"total_count\":864,\"total_amount\":32400,\"commision\":17280},\"Refund\":{\"commision_tax\":1555.2,\"pc_fees\":4320,\"pc_tax\":734.4,\"total_count\":864,\"total_amount\":32400,\"commision\":17280}}");
        j.put("Primary_Key", random(2));
        log.info(j.toString());
        kafkaTemplate.send(TOPIC,UUID.randomUUID().toString(), j.toString());

        return "Published Successfully";
    }

    @GetMapping("/save/{message}")
    public String saveRedis(@PathVariable("message")
                                 final String message)
    {

        // Sending the message
        redisTemplate.opsForValue().set(message,"hello -> "+message);

        return "Saved Successfully";
    }

    @Autowired
    private RedisScript<Boolean> script;
    @GetMapping("/transfer")
    public String save()
    {
        Random rand1 = new Random();
        String rand = random(7);
        String randomno = String.valueOf(Math.random());
        int rand_int1 = rand1.nextInt(1000);
        System.out.println("Random value is -> "+rand_int1);
        String a = redisTemplate.execute(script, List.of(rand),String.valueOf(rand_int1)).toString();
        System.out.println("Response from REDIS -> " + a);
        return "success";
    }

    public String random(int len)
    {
        // create a string of all characters
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = len;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphabet.length());

            // get character specified by index
            // from the string
            char randomChar = alphabet.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        String randomString = sb.toString();
        log.info("Random String is: " + randomString);
        return randomString;
    }
}
