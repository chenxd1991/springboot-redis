package com.chenxd.springbootredis;

import com.chenxd.springbootredis.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

@SpringBootTest
class SpringbootRedisApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    public void redisTest(){
        User user = new User();
        user.setName("陈晓东");
        user.setBirthday(new Date());
    }
}
