package com.fexco.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomCache implements Cache {

    private static final Logger LOG = LoggerFactory.getLogger(CustomCache.class);

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(1);

    private String name;
    private CacheRepository cacheRepository;

    Map<Object, Object> cache = Collections.synchronizedMap(new HashMap<>());

    static ResponseEntity<?> entryToEntity(CacheEntry entry) {
        return new ResponseEntity<String>(entry.getValue(), HttpStatus.valueOf(entry.getStatusCode()));
    }

    static CacheEntry entityToEntry(String key, ResponseEntity<?> entity) {
        if (entity.getBody() instanceof String)
            return new CacheEntry(key, (String) entity.getBody(), entity.getStatusCode().name());
        else
            return new CacheEntry(key, null, entity.getStatusCode().name());
    }

    CustomCache(String name, CacheRepository cacheRepository) {
        this.name = name;
        this.cacheRepository = cacheRepository;

        if (cacheRepository != null)
            loadDataFromRepository();
    }

    private void loadDataFromRepository() {

        EXECUTOR.submit(() -> {

            LOG.info("there are {} items in the repository", cacheRepository.count());

            for (CacheEntry entry : cacheRepository.findAll()) {
                LOG.info("loading key {} from persistent cache", entry.getKey());
                cache.put(entry.getKey(), entryToEntity(entry));
            }
        });
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        if (cache.containsKey(key))
            return () -> cache.get(key);
        else
            return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return type.cast(cache.get(key));
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        // ignore value loader, all the records should be in memory
        return (T) cache.get(key);
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);

        if (cacheRepository != null)
            saveDataToRepository(key, value);
    }

    private void saveDataToRepository(Object key, Object value) {
        EXECUTOR.submit(() -> {
            LOG.info("storing key {} in persistent cache", key);
            cacheRepository.save(entityToEntry((String) key, (ResponseEntity) value));
        });
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return () -> value;
    }

    @Override
    public void evict(Object key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
