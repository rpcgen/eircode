package com.fexco.cache

import spock.lang.Specification

class CustomCacheManagerTest extends Specification {

    void "custom manager must return a custom cache instance"() {
        expect:
        new CustomCacheManager().getCache('my-cache') as CustomCache
    }

    void "custom manager must set load address-cache when prefetch operation is called"() {
        CustomCacheManager customCacheManager = new CustomCacheManager()
        customCacheManager.prefetchCache()

        expect:
        customCacheManager.cacheNames.contains('address-cache')
    }

}
