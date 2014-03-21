package <%=packageName%>.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.URI;

@Configuration
@EnableCaching
@AutoConfigureAfter(MetricsConfiguration.class)
public class CacheConfiguration {
    @Value("<%= _.unescape('\$\{redis.url:redis://localhost:6379}') %>")
    private String redisUrl;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() throws Exception {
        URI redisURI = new URI(redisUrl);

        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisURI.getHost());
        factory.setPort(redisURI.getPort());
        factory.setUsePool(true);

        if (redisURI.getUserInfo() != null) {
            factory.setPassword(redisURI.getUserInfo().split(":", 2)[1]);
        }

        return factory;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() throws Exception {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager() throws Exception {
        //Don't fail if redis is not available
        try {
            redisTemplate().getConnectionFactory().getConnection();
        } catch (RedisConnectionFailureException e) {
            return new NoOpCacheManager();
        }
        return new RedisCacheManager(redisTemplate());
    }
}
