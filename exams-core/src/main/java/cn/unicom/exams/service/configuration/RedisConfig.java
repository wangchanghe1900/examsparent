package cn.unicom.exams.service.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
	private Duration timeToLive = Duration.ZERO;
	
    public Duration getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(Duration timeToLive) {
		this.timeToLive = timeToLive;
	}

	@Bean
	@ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws Exception {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(keySerializer());
        template.setHashKeySerializer(keySerializer());
        template.setValueSerializer(valueSerializer());
        template.setHashValueSerializer(valueSerializer());
        return template;
    }
    
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                .disableCachingNullValues(); // 设置缓存有效期三分钟
/*        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();*/
        RedisCacheManager redisCacheManager= RedisCacheManager.builder(redisConnectionFactory)
        		.cacheDefaults(redisCacheConfiguration)
        		.transactionAware()
        		.build();
        return redisCacheManager;
    }
    
    
    private RedisSerializer<String> keySerializer(){
    	return new StringRedisSerializer();
    }
    
    private RedisSerializer<Object> valueSerializer(){
    	return new GenericJackson2JsonRedisSerializer();
    }


}