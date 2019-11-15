package com.example.redis.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jackie wang
 * @Title: RedisController
 * @ProjectName redis-spring-boot-starter-master
 * @Description: 使用自定义组件封装后的工具类StringRedisTemplate
 * @date 2019/10/30 16:59
 */
@RestController
@RequestMapping("string/redis")
public class StringRedisController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "set", method = RequestMethod.GET)
    public String set() {
        stringRedisTemplate.opsForValue().set("string-redis-hello", "hello world");
        return "success";
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String get() {
        return stringRedisTemplate.opsForValue().get("string-redis-hello");
    }

}
