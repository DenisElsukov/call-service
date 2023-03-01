package com.cs.consumer.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

    private final String redisAddress;
    private final String password;

    public RedisConfiguration(
        @Value("${com.cs.consumer.redis.server}") final String redisAddress,
        @Value("${com.cs.consumer.redis.password}") final String password) {
        this.redisAddress = redisAddress;
        this.password = password;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(redisAddress).setPassword(password);

        return Redisson.create(config);
    }

}
