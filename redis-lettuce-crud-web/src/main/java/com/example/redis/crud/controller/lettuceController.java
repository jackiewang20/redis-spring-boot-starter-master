//package com.example.redis.crud.controller;
//
//import com.lambdaworks.redis.RedisClient;
//import com.lambdaworks.redis.api.StatefulRedisConnection;
//import com.lambdaworks.redis.api.sync.RedisCommands;
//import com.lambdaworks.redis.cluster.RedisClusterClient;
//import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
//import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.connection.RedisStringCommands;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author jackie wang
// * @Title: lettuceController
// * @ProjectName redis-spring-boot-starter-master
// * @Description: 使用自定义组件中依赖的原生lettuce，具体使用可以进行封装。
// * 示例详细参考：https://github.com/lettuce-io/lettuce-core
// * 注意：redis集群版和单机版不允许混用。
// * @date 2019/11/1 11:27
// */
//@RestController
//@RequestMapping("lettuce")
//public class lettuceController {
//    @Autowired
//    RedisClusterClient redisClusterClient;
//    @Autowired
//    RedisClient redisClient;
//
//    /** 集群版redis */
//    @RequestMapping(value = "cluster/set", method = RequestMethod.GET)
//    public String cset() {
//        StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect();
//        RedisAdvancedClusterCommands<String, String> commands = connection.sync();
//        commands.set("lettuce:hello", "lettuce is good.");
//        connection.close();
//
//        return "success";
//    }
//
//    @RequestMapping(value = "cluster/get", method = RequestMethod.GET)
//    public String cget() {
//        StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect();
//        RedisAdvancedClusterCommands<String, String> commands = connection.sync();
//        String result = commands.get("lettuce:hello");
//        connection.close();
//
//        return result;
//    }
//
//    /** 单机版redis */
//    @RequestMapping(value = "set", method = RequestMethod.GET)
//    public String set() {
//        StatefulRedisConnection<String, String> connection = redisClient.connect();
//        RedisCommands<String, String> commands = connection.sync();
//        commands.set("lettuce:single:hello", "[单机版]lettuce is good.");
//        connection.close();
//
//        return "success";
//    }
//
//    @RequestMapping(value = "get", method = RequestMethod.GET)
//    public String get() {
//        StatefulRedisConnection<String, String> connection = redisClient.connect();
//        RedisCommands<String, String> commands = connection.sync();
//        String result = commands.get("lettuce:single:hello");
//        connection.close();
//
//        return result;
//    }
//
//}
