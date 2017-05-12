package com.fexco.alliesComputing;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class AddressKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {

        if (params == null || params.length < 3)
            throw new IllegalArgumentException("Expected four params to generate a proper key");

        Map<String, Object> key = new HashMap<>();
        key.put("alpha2",   params[0]);
        key.put("fragment", params[1]);
        key.put("options",  params[2]);
        return key.toString();
    }
}
