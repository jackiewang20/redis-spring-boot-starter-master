package com.example.redis.crud.starter.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.time.Duration;

/**
 * @author jackie wang
 * @Title: RedisAutoConfiguration
 * @ProjectName redis-spring-boot-starter-master
 * @Description: Redis客户端自动配置，默认使用lettuce客户端库；配置RedisTemplate，StringRedisTemplate客户端工具类。
 * @date 2019/10/30 11:36
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class) // 激活配置属性类
public class RedisAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    /**
     * 1.redis cluster配置。
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "spring.redis.cluster.nodes") // 条件加载配置属性，如果满足条件，加载当前bean
    public RedisClusterConfiguration redisClusterConfiguration() {
        RedisProperties.Cluster clusterProperties = redisProperties.getCluster();
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterProperties.getNodes());
        if (!StringUtils.isEmpty(clusterProperties.getMaxRedirects())) {
            redisClusterConfiguration.setMaxRedirects(clusterProperties.getMaxRedirects());
        }
        // 设置密码
        redisClusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));

        return redisClusterConfiguration;
    }

    /**
     * 2.集群版配置RedisConnectionFactory，使用JedisConnectionFactory作为redis连接。
     * 说明：redisClusterConfiguration是一个定义的bean。
     *
     * @param redisClusterConfiguration
     * @return
     */
    @Bean
//@ConditionalOnProperty(value = "spring.redis.cluster.nodes[0]") // 条件加载配置属性，如果满足条件，加载当前bean
    @ConditionalOnProperty(value = "spring.redis.cluster.nodes") // 条件加载配置属性，如果满足条件，加载当前bean
    public LettuceConnectionFactory lettuceConnectionFactory(
            RedisClusterConfiguration redisClusterConfiguration) {

//        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisClusterConfiguration);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisClusterConfiguration, getPoolConfig());

        return factory;
    }

    /**
     * 2.单机版配置RedisConnectionFactory，使用LettuceConnectionFactory作为redis连接。
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "spring.redis.host")
    @ConditionalOnMissingBean(LettuceConnectionFactory.class) // 如果集群模式LettuceConnectionFactory bean创建，当前方法不执行
    public LettuceConnectionFactory lettuceConnectionFactoryWithStandalone() {
        /**
         * 单机模式:
         * 1.spring boot 2.x导入包import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
         * 2.spring boot 1.5.x本部根据2.x的RedisStandaloneConfiguration进行自定义。
         */

        /** spring boot 2.x */
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setDatabase(redisProperties.getDatabase());
        // 设置密码
        configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));

//        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, getPoolConfig());
        return factory;
    }

    /**
     * 3.redisTemplate客户端工具类配置：推荐map，set，zset，list存储类型使用。
     * 说明：redisConnectionFactory是一个已定义的bean，这里使用LettuceConnectionFactory。
     *
     * @param redisConnectionFactory
     * @return
     * @throws UnknownHostException
     */
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        ObjectMapper om = new ObjectMapper();
        // 设置任何字段可见
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 设置不是final的属性可以转换
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key：采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // value：采用String的序列化方式
        template.setValueSerializer(stringRedisSerializer);
        // hash key：采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // hash value：采用jackson的序列化方式
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        template.setEnableTransactionSupport(true);

        return template;
    }

    /**
     * 3.2.stringRedisTemplate客户端工具类配置：推荐String存储类型使用。
     * 说明：redisConnectionFactory是一个已定义的bean，这里使用LettuceConnectionFactory。
     *
     * @param redisConnectionFactory
     * @return
     * @throws UnknownHostException
     */
    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        RedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key：采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // value：采用String的序列化方式
        template.setValueSerializer(stringRedisSerializer);
        // hash key：采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // hash value：采用jackson的序列化方式
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        return template;
    }


    /**
     * 获取缓存连接池，spring boot 2.x配置
     *
     * @return
     */
    @Bean
    public LettucePoolingClientConfiguration getPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        config.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
        config.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        config.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());

        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config)
                .commandTimeout(redisProperties.getTimeout())
                .shutdownTimeout(redisProperties.getTimeout())
                .build();
        return lettucePoolingClientConfiguration;
    }

}
