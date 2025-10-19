package per.liyl.configuration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import per.liyl.database.entities.AppleOrder;

import java.util.concurrent.TimeUnit;

@Configuration
public class GuavaLocalCacheConfiguration {

    @Bean
    public Cache<Long, AppleOrder> appleOrderCache(){
        // 创建缓存：设置过期时间 1 分钟，最大容量 100 个条目
        return CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

}
