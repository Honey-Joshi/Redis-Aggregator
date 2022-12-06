package com.paytm.pg.aggregator.Processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONException;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
        StopWatch st = new StopWatch("abcd");
        st.start();
            JSONObject jsonObject = new JSONObject(message);
            String uk = jsonObject.getString("Primary_Key");
            HashMap<String,String> hm = new HashMap<>();
            hm = rec(jsonObject,hm,"");
            log.info("Size of hashmap is -> {}",hm.size());

            JSONObject finalObj = new JSONObject(hm);

            String s = redisTemplate.execute(redisScript, List.of(uk),finalObj.toString(),key).toString();
            log.info(" LUA Response-> "+s);

            st.stop();

            log.info("Total time taken in ms -> "+st.getTotalTimeMillis());
            log.info("Total time taken in nanoSeconds -> "+st.getTotalTimeNanos());

    }

    public boolean isValid(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }


    public HashMap<String,String> rec(JSONObject obj,HashMap<String,String> hm,String prev)
    {
        Set<String> keys = obj.keySet();
        for(String s : keys)
        {
            String pre = obj.get(s).toString();
            if (isValid(pre))
            {
                rec(obj.getJSONObject(s),hm,s);
            }
            else if (isJSONArray(pre))
            {
                JSONArray arr = obj.getJSONArray(s);
                int len = arr.length();
                int pp = 0;
                while(pp < len)
                {

                    JSONObject qq= arr.getJSONObject(pp);
                    rec(manipulateArray(qq),hm,s);
                    pp+=1;

                }
            }
            else
            {

                prev.concat("_");
                hm.put(prev.concat(s.toString()),obj.get(s).toString());
            }
        }
        return hm;
    }

    public JSONObject manipulateArray(JSONObject js)
    {

        String sot = js.get("subOrderType").toString();
        JSONObject pp = new JSONObject();
        for(String s : js.keySet())
        {
            String newS = s+"_"+sot;
            Object newVal = js.get(s);
            pp.put(newS,newVal);
        }
        return pp;
    }

    public boolean isJSONArray(String input) {
        try {
            new JSONArray(input);
            return true;
        } catch (JSONException ex) {
            return false;
        }
    }

}