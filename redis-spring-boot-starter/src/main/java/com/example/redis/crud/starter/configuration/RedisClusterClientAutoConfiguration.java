//package com.example.redis.crud.starter.configuration;
//
//import com.lambdaworks.redis.RedisClient;
//import com.lambdaworks.redis.RedisURI;
//import com.lambdaworks.redis.cluster.RedisClusterClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.Assert;
//import org.springframework.util.StringUtils;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author jackie wang
// * @Title: RedisClusterClientAutoConfiguration
// * @ProjectName redis-spring-boot-starter-master
// * @Description: 使用lettuce原生客户端包，RedisClusterClient bean定义。
// * 说明：当前示例兼容性不好，不推荐使用；如果RedisClusterClient，和RedisClient bean都加载到spring中，
// * 客户端同时使用了RedisClusterClient和RedisClient bean进行数据库操作，需要配置集群版和单机版配置，这样就
// * 强制依赖配置了。
// * @date 2019/11/1 13:08
// */
//@Configuration
//@EnableConfigurationProperties(RedisProperties.class)
//public class RedisClusterClientAutoConfiguration {
//
//    @Autowired
//    private RedisProperties redisProperties;
//
//    /**
//     * redis cluster客户端
//     * @return
//     */
//    @Bean
//    @ConditionalOnProperty(value = "spring.redis.cluster.nodes") // 条件加载配置属性，如果满足条件，加载当前bean
//    public RedisClusterClient redisClusterClient() {
//        List<String> hostAndPorts = redisProperties.getCluster().getNodes();
//        Set<RedisURI> redisURIS = new HashSet<>();
//        // 添加个redis节点链接配置信息
//        for (String hostAndPort : hostAndPorts) {
//            /** 实例化RedisURI，并初始化属性值 */
//            RedisURI redisURI = getRedisURI(hostAndPort, redisProperties.getTimeout());
//
//            if (StringUtils.hasText(redisProperties.getPassword())) {
//                redisURI.setPassword(redisProperties.getPassword());
//            }
//
//            redisURIS.add(redisURI);
//        }
//
//        return RedisClusterClient.create(redisURIS);
//    }
//
//    @Bean
//    @ConditionalOnProperty(value = "spring.redis.host")
////    @ConditionalOnMissingBean(RedisClusterClient.class) // 如果集群模式RedisClusterClient bean创建，则不执行
//    public RedisClient redisClient() {
//        /** 不推荐集群版和单机版同时使用 */
//        // 当前已经配置了redis集群版链接配置，不允许同时配置单机版链接配置
////        Assert.isTrue(redisProperties.getCluster().getNodes()==null,
////                "Redis cluster version link configuration is currently configured. " +
////                        "It is not allowed to configure stand-alone version link configuration at the same time." +
////                        "[Solution]: ****disable the redis cluster or redis stand-alone configuration.****");
//
//        RedisURI redisURI = new RedisURI();
//        redisURI.setHost(redisProperties.getHost());
//        redisURI.setPort(redisProperties.getPort());
//        redisURI.setTimeout(redisProperties.getTimeout());
//        redisURI.setUnit(TimeUnit.SECONDS);
//        if (StringUtils.hasText(redisProperties.getPassword())) {
//            redisURI.setPassword(redisProperties.getPassword());
//        }
//        redisURI.setDatabase(redisProperties.getDatabase());
//        return RedisClient.create(redisURI);
//    }
//
//    /**
//     * 实例化一个RedisURI。
//     * @param hostAndPort
//     * @param timeout
//     * @return
//     */
//    private RedisURI getRedisURI(String hostAndPort, int timeout) {
//        String[] myHostAndPort = hostAndPort.split(":");
//        Assert.notNull(myHostAndPort, "HostAndPort need to be seperated by  ':'.");
//        Assert.isTrue(myHostAndPort.length==2, "Invalid host address, for example: '192.168.10.1:7000'");
//
//        return new RedisURI(myHostAndPort[0], Integer.valueOf(myHostAndPort[1]),timeout, TimeUnit.SECONDS);
//    }
//
//}
