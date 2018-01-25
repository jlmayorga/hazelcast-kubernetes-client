package org.jlmayorga.cache.client.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

@Configuration
public class CacheConfig {

    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    public CacheManager getCacheManager() {
        CacheManager cacheManager = null;

        try {
            System.setProperty("hazelcast.jcache.provider.type", "client");
            CachingProvider cachingProvider = Caching.getCachingProvider();
            cacheManager = cachingProvider.getCacheManager();
        } catch (CacheException e) {
            log.error("Cache Manager couldn't be instantiated, caching will be disabled", e);
        }

        return cacheManager;
    }
}
