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
        JSONObject j = new JSONObject(new String("{\n" +
                "\t\"orderType\": \"ACQUIRING\",\n" +
                "\t\"totalCount\": \"4\",\n" +
                "\t\"totalAmount\": {\n" +
                "\t\t\"currency\": \"INR\",\n" +
                "\t\t\"value\": \"188500\"\n" +
                "\t},\n" +
                "\t\"totalCommission\": {\n" +
                "\t\t\"currency\": \"INR\",\n" +
                "\t\t\"value\": \"0\"\n" +
                "\t},\n" +
                "\t\"totalCommissionTax\": {\n" +
                "\t\t\"currency\": \"INR\",\n" +
                "\t\t\"value\": \"0\"\n" +
                "\t},\n" +
                "\t\"totalPcFee\": {\n" +
                "\t\t\"currency\": \"INR\",\n" +
                "\t\t\"value\": \"0\"\n" +
                "\t},\n" +
                "\t\"totalPcTax\": {\n" +
                "\t\t\"currency\": \"INR\",\n" +
                "\t\t\"value\": \"0\"\n" +
                "\t},\n" +
                "\t\"subOrderWiseBreakup\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"subOrderType\": \"INSTANT_SETTLEMENT\",\n" +
                "\t\t\t\"totalCount\": \"3\",\n" +
                "\t\t\t\"totalAmount\": {\n" +
                "\t\t\t\t\"currency\": \"INR\",\n" +
                "\t\t\t\t\"value\": \"179000\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"totalCommission\": {\n" +
                "\t\t\t\t\"currency\": \"INR\",\n" +
                "\t\t\t\t\"value\": \"0\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"totalCommissionTax\": {\n" +
                "\t\t\t\t\"currency\": \"INR\",\n" +
                "\t\t\t\t\"value\": \"0\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"totalPcFee\": {\n" +
                "\t\t\t\t\"currency\": \"INR\",\n" +
                "\t\t\t\t\"value\": \"0\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"totalPcTax\": {\n" +
                "\t\t\t\t\"currency\": \"INR\",\n" +
                "\t\t\t\t\"value\": \"0\"\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"subOrderType\": \"EMPTY\",\n" +
                "\t\t\t\"totalCount\": \"1\",\n" +
                "\t\t\t\"totalAmount\": {\n" +
                "\t\t\t\t\"currency\": \"INR\",\n" +
                "\t\t\t\t\"value\": \"9500\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"totalCommission\": {\n" +
                "\t\t\t\t\"currency\": \"INR\",\n" +
                "\t\t\t\t\"value\": \"0\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"totalCommissionTax\": {\n" +
                "\t\t\t\t\"currency\": \"INR\",\n" +
                "\t\t\t\t\"value\": \"0\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"totalPcFee\": {\n" +
                "\t\t\t\t\"currency\": \"INR\",\n" +
                "\t\t\t\t\"value\": \"0\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"totalPcTax\": {\n" +
                "\t\t\t\t\"currency\": \"INR\",\n" +
                "\t\t\t\t\"value\": \"0\"\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}"));
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
