package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;


@Configuration
@EnableCaching
public abstract class CacheConfig implements CachingConfigurer {
    @Bean
    public CacheManager likeStatusCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Collections.singletonList(new ConcurrentMapCache("likeStatus"))); // default cache
        return cacheManager;
    }

    @Bean
    public CacheManager dislikeStatusCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Collections.singletonList(new ConcurrentMapCache("dislikeStatus"))); // default cache
        return cacheManager;
    }

    @Bean
    public CacheManager examcacheManager() {
        return new ConcurrentMapCacheManager("examsCache");
    }


    @Bean
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }
//    @Bean
//    @Primary
//    public RedisCacheManager likeStatusCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
//                .initialCacheNames(Collections.singleton("likeStatus"))
//                .build();
//        return cacheManager;
//    }
//
//    @Bean
//    @Primary
//    public RedisCacheManager dislikeStatusCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
//                .initialCacheNames(Collections.singleton("dislikeStatus"))
//                .build();
//        return cacheManager;
//    }
}
