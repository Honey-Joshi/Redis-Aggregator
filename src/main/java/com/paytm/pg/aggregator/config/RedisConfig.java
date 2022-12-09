package com.paytm.pg.aggregator.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan
@Slf4j
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisClusterConnectionFactory() {

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.clusterNode("172.18.0.4",6379);
        RedisNode r1 = new RedisClusterNode("172.18.0.5",6379);
//        RedisNode r2 = new RedisClusterNode("10.211.126.79",7001);
//        RedisNode r3 = new RedisClusterNode("10.211.125.160",7001);
//        RedisNode r4 = new RedisClusterNode("10.211.125.160",7000);
//        RedisNode r5 = new RedisClusterNode("10.211.123.4",7001);
//        redisClusterConfiguration.addClusterNode(r2);
        redisClusterConfiguration.addClusterNode(r1);
//        redisClusterConfiguration.addClusterNode(r3);
//        redisClusterConfiguration.addClusterNode(r4);
//        redisClusterConfiguration.addClusterNode(r5);
        redisClusterConfiguration.setMaxRedirects(5);
        redisClusterConfiguration.setPassword("bitnami");
        // Support adaptive cluster topology refresh and static refresh source
        //In case a master node failure this configuration manage the whole topology for otherher nodes.
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions =  ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh()
                .enableAllAdaptiveRefreshTriggers()
                .refreshPeriod(Duration.ofSeconds(15))
                .build();

        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                .topologyRefreshOptions(clusterTopologyRefreshOptions).build();


        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.MASTER)
                .clientOptions(clusterClientOptions).build();
        log.info("in Redis Config");
        log.info("Cluster nodes -> "+redisClusterConfiguration.getClusterNodes().toString());
        return new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
    }



    @Bean
    @Primary
    public RedisTemplate<String, Object> redisClusterTemplate(@Qualifier("redisClusterConnectionFactory") LettuceConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Map<String, Object>> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Map.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        log.info("Hi, in redis bean");
        return redisTemplate;
    }

    @Bean
    public RedisScript<Boolean> script() {
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("Transfer.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }

}
