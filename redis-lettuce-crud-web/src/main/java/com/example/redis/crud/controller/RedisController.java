package com.example.redis.crud.controller;

import com.example.redis.crud.bean.UserVo;
import com.example.redis.crud.starter.component.RedisTemplateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @author jackie wang
 * @Title: RedisController
 * @ProjectName redis-spring-boot-starter-master
 * @Description: 使用自定义组件封装后的工具类RedisTemplate，RedisTemplateUtil
 * @date 2019/10/30 16:59
 */
@RestController
@RequestMapping("redis")
public class RedisController {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisTemplateUtil redisTemplateUtil;

    @RequestMapping(value = "set", method = RequestMethod.GET)
    public String set() {
        redisTemplate.opsForValue().set("redis-hello", "hello world");
        return "success";
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String get() {
        return (String) redisTemplate.opsForValue().get("redis-hello");
    }

    @RequestMapping(value = "set2", method = RequestMethod.GET)
    public String set2() {
        redisTemplateUtil.set("redis-hello2", "hello world");
        return "success";
    }

    @RequestMapping(value = "get2", method = RequestMethod.GET)
    public String get2() {
        return redisTemplateUtil.get("redis-hello2");
    }

    @RequestMapping(value = "hmset", method = RequestMethod.GET)
    public String hmset() {
        UserVo userVo = new UserVo();
        ObjectMapper mapper = new ObjectMapper();
        userVo.setId(1);
        userVo.setName("张三");
        userVo.setDate(new Date());
        Map<String, Object> userVoMap = mapper.convertValue(userVo, Map.class);
        redisTemplateUtil.hmset("zhangsan", userVoMap);
        return "success";
    }

    @RequestMapping(value = "hmget", method = RequestMethod.GET)
    public String hmget() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(redisTemplateUtil.hmget("zhangsan"));
        return jsonString;
    }

    @RequestMapping(value = "hmset2", method = RequestMethod.GET)
    public String hmset2() {
        UserVo userVo = new UserVo();
        ObjectMapper mapper = new ObjectMapper();
        userVo.setId(1);
        userVo.setName("张三");
        userVo.setDate(new Date());
        Map<String, Object> userVoMap = mapper.convertValue(userVo, Map.class);
        redisTemplate.opsForHash().putAll("zhangsan2", userVoMap);
        return "success";
    }

    @RequestMapping(value = "hmget2", method = RequestMethod.GET)
    public String hmget2() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(redisTemplate.opsForHash().entries("zhangsan2"));
        return jsonString;
    }
}
