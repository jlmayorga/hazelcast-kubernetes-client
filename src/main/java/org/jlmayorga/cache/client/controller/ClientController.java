package org.jlmayorga.cache.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class ClientController {
    //docker run -ti -p 5701:5701 hazelcast/hazelcast
    private CacheManager cacheManager;

    @Autowired
    public ClientController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @RequestMapping("/service")
    public Map<String, String> getValue(@RequestParam String id) {
        Map<String, String> resultMap = new HashMap<>();
        Boolean fromCache = Boolean.FALSE;
        String value = null;
        Cache<String, String> cache = null;
        if (cacheManager != null) {
            cache = cacheManager.getCache("sample-cache", String.class, String.class);
            if (cache == null) {

                MutableConfiguration<String, String> config = new MutableConfiguration<>();
                config.setStoreByValue(true)
                        .setTypes(String.class, String.class)
                        .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.HOURS, 24)))
                        .setStatisticsEnabled(true).setManagementEnabled(true);

                cache = cacheManager.createCache("sample-cache", config);
            }
            value = cache.get(id);
            if (value != null) {
                fromCache = Boolean.TRUE;
            }
        }

        if (value == null) {
            Instant instant = Instant.now();
            value = Timestamp.from(instant).toString();
            if (cache != null) {
                cache.put(id, value);
            }

        }

        resultMap.put(id, value);
        resultMap.put("fromCache", fromCache.toString());

        return resultMap;
    }
}
