package com.fexco.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomCacheManager implements CacheManager {

    private static final Logger LOG = LoggerFactory.getLogger(CustomCacheManager.class);

    private CacheRepository cacheRepository;

    @Autowired
    public void setCacheRepository(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    private Map<String, CustomCache> caches = new HashMap<>();

    @PostConstruct
    public void prefetchCache() {
        getCache("address-cache");
    }

    @Override
    public Cache getCache(String name) {

        LOG.info("getting custom cache for {} ", name);

        if (!caches.containsKey(name))
            caches.put(name, new CustomCache(name, cacheRepository));

        return caches.get(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        return caches.keySet();
    }
}
