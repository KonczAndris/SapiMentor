package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;


@Configuration
@EnableCaching
public abstract class CacheConfig implements CachingConfigurer {
    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(100);
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

}
